package pw.io.booker.controller;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import pw.io.booker.model.Customer;
import pw.io.booker.repo.CustomerRepository;
import pw.io.booker.util.TokenCreator;

@RestController
@RequestMapping("/customers")
public class CustomerController {

  private CustomerRepository customerRepository;

  public CustomerController(CustomerRepository customerRepository) {
    super();
    this.customerRepository = customerRepository;
  }

  @GetMapping
  @Transactional(readOnly=true)
  public List<Customer> getAll(@RequestHeader("token") String token) {
    return (List<Customer>) customerRepository.findAll();
  }

  @PostMapping
  public List<Customer> saveAll(@RequestBody List<Customer> customers) {
    for(Customer customer : customers) {
      if(customerRepository.findById(customer.getCustomerId()).isPresent()) {
        throw new RuntimeException("Customers already exist");
      }
    }
    return (List<Customer>) customerRepository.saveAll(customers);
  }

  @PutMapping
  @Transactional
  public List<Customer> updateAll(@RequestHeader("token") String token, @RequestBody List<Customer> customers) {
    for(Customer customer : customers) {
      if(!customerRepository.findById(customer.getCustomerId()).isPresent()) {
        throw new RuntimeException("Customers should exist first");
      }
    }
    return (List<Customer>) customerRepository.saveAll(customers);
  }

  @DeleteMapping
  @Transactional
  public List<Customer> deleteAll(@RequestHeader("token") String token, @RequestParam("customerIdList") List<Integer> customerIdList) {
    List<Customer> customerList = (List<Customer>) customerRepository.findAllById(customerIdList);
    customerRepository.deleteAll(customerList);
    return customerList;
  }

  @GetMapping("/{customerId}")
  @Transactional(readOnly=true)
  public Customer getCustomer(@RequestHeader("token") String token, @PathVariable("customerId") int customerId) {
    return customerRepository.findById(customerId).get();
  }

  @PutMapping("/{customerId}")
  @Transactional
  public Customer updateCustomer(@RequestHeader("token") String token, @PathVariable("customerId") int customerId,
      @RequestBody Customer customer) {
    if(customerId != customer.getCustomerId()) {
      throw new RuntimeException("Id is not the same with the object id");
    }
    if (!customerRepository.findById(customer.getCustomerId()).isPresent()) {
      throw new RuntimeException("Customers should exist first");
    }
    customer.setCustomerId(customerId);
    return customerRepository.save(customer);
  }

  @DeleteMapping("/{customerId}")
  @Transactional
  public Customer deleteCustomer(@RequestHeader("token") String token, @PathVariable("customerId") int customerId) {
    Customer customer = customerRepository.findById(customerId).get();
    customerRepository.delete(customer);
    return customer;
  }
}
