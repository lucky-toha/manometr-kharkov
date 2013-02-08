package ua.com.manometer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import ua.com.manometer.model.Customer;
import ua.com.manometer.model.OrgForm;
import ua.com.manometer.model.User;
import ua.com.manometer.model.address.Area;
import ua.com.manometer.model.address.City;
import ua.com.manometer.model.address.Country;
import ua.com.manometer.model.address.Region;
import ua.com.manometer.service.CustomerService;
import ua.com.manometer.service.OrgFormService;
import ua.com.manometer.service.UserService;
import ua.com.manometer.service.address.AreaService;
import ua.com.manometer.service.address.CityService;
import ua.com.manometer.service.address.CountryService;
import ua.com.manometer.service.address.RegionService;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;
    @Autowired
    private OrgFormService orgFormService;
    @Autowired
    private UserService userService;
    @Autowired
    private CountryService countryService;
    @Autowired
    private RegionService regionService;
    @Autowired
    private CityService cityService;
    @Autowired
    private AreaService areaService;



//    @InitBinder
//    public void initBinder(WebDataBinder dataBinder){
//        dataBinder.registerCustomEditor(Profession.class, editor);
//    }

    @RequestMapping("/")
    public String populateCustomers(Map<String, Object> map) {
        map.put("listCustomer", customerService.listCustomer());
        return "customers";
    }

    @RequestMapping("/edit")
    public String setupForm(@RequestParam(value = "id", required = false) Integer id, ModelMap model) {
        model.put("orgForms", orgFormService.listOrgForm());
        model.put("branches", Customer.branchValues);
        model.put("users", userService.listUser());
        List<Country> countries = countryService.listCountry();
        model.put("countries", countries);
        model.put("regions", regionService.listRegion());


        if (id == null) {
            List<Area> areas = areaService.listAreaForCountry(countries.get(0).getId());
            model.put("areas", areas);
            model.put("cities", cityService.listCityForArea(areas.get(0).getId()));
            Customer customer = new Customer();
            customer.setOldRecord(new Customer());
            customer.setHeadCustomer(new Customer());

            OrgForm orgForm = new OrgForm();
            orgForm.setId(1);
            customer.setOrgForm(orgForm);
            //todo check
            User user = new User();
            user.setId(1);
            customer.setPerson(user);
            customer.setDateLastCorrection(new Date());
            customer.setDateOfRecord(new Date());
            customer.setMergeData(new Date());
            model.put("customer", customer);
        } else {

            final Customer customer = customerService.getCustomer(id);
            List<Area> areas = areaService.listAreaForCountry(customer.getCountry());
            model.put("areas", areas);
            List<City> cities = cityService.listCityForArea(customer.getArea());
            model.put("cities", cities);
            if (customer.getOldRecord() == null) {
                customer.setOldRecord(new Customer());
            }
            if (customer.getHeadCustomer() == null) {
                customer.setHeadCustomer(new Customer());
            }
            model.put("customer", customer);
        }
        return "editCustomer";
    }


    @RequestMapping("/add")
    public String processSubmit(@ModelAttribute("customer") Customer customer) {
        customerService.addCustomer(customer);
        return "redirect:/customers/";
    }


    @RequestMapping("/listCity")
    public
    @ResponseBody
    List<City> listCity(@RequestParam("area") Integer areaId) {
        return cityService.listCityForArea(areaId);
    }

    @RequestMapping("/listCustomers")
    public
    @ResponseBody
    List listCustomer(@RequestParam("term") String customerTemplate) {

        customerTemplate = decode(customerTemplate);
        List<String> result = customerService.findByShortNameExample(customerTemplate);
        return result;
    }


    @RequestMapping("/listArea")
    public
    @ResponseBody
    List<Area> listArea(@RequestParam("country") Integer countryId) {
        return areaService.listAreaForCountry(countryId);
    }

    @RequestMapping("/add_city")
    public
    @ResponseBody
    City addCity(City city) {
        cityService.addCity(city);
        return city;
    }

    @RequestMapping("/add_area")
    public
    @ResponseBody
    Area addArea(Area area) {
        areaService.addArea(area);
        return area;
    }

    @RequestMapping("/add_country")
    public
    @ResponseBody
    Country addCountry(Country country) {
        countryService.addCountry(country);
        return country;
    }

    @RequestMapping("/add_region")
    public
    @ResponseBody
    Region addRegion(Region region) {
        regionService.addRegion(region);
        return region;
    }

    @RequestMapping("/add_org_form")
    public
    @ResponseBody
    OrgForm addOrgForm(OrgForm orgForm) {
        orgFormService.addOrgForm(orgForm);
        return orgForm;
    }


    private String decode(String s) {
        String result = null;
        String ENCODING_iso_8859_1 = "ISO-8859-1";
        String ENCODING_UTF8 = "UTF-8";

        try {
            result = new String(s.getBytes(ENCODING_iso_8859_1), ENCODING_UTF8);
        } catch (UnsupportedEncodingException e) {

            e.printStackTrace();
        }
        return result;
    }

}
