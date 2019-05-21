package com.jdev.microservice.contact.controller

import com.jdev.microservice.contact.model.dto.ContactDTO
import com.jdev.microservice.contact.service.ContactService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpEntity
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/contact")
class ContactController {

    @Autowired
    lateinit var contactService: ContactService

    val LOG = LoggerFactory.getLogger(this::class.java)

    @GetMapping("/")
    @ResponseBody
    fun getAllRequest(): HttpEntity<*> {
        LOG.trace("--- GET ALL ---")
        val result = contactService.listAllContact()
        return if (result.isNotEmpty()) {
            LOG.trace("OK")
            HttpEntity(result)
        } else{
            LOG.trace("NO CONTENT")
            HttpEntity(HttpStatus.NO_CONTENT)
        }
    }

    @PostMapping("/save")
    @ResponseBody
    fun saveRequest(@RequestBody contactDTO: ContactDTO): HttpEntity<*> {
        LOG.trace("--- SAVE ---")
        val response: ContactDTO? = contactService.saveContact(contactDTO) as ContactDTO

        return if (response != null) {
            LOG.trace("OK")
            HttpEntity(response)
        } else {
            LOG.trace("BAD REQUEST")
            HttpEntity(HttpStatus.BAD_REQUEST)
        }
    }

    @GetMapping("/find")
    @ResponseBody
    fun getContactRequest(@RequestParam(name = "name") name: String): HttpEntity<*> {
        LOG.trace("--- GET CONTACT ---")
        val contactDTO = contactService.findByNameContact(name) as ContactDTO?

        return if (contactDTO == null) {
            LOG.trace("NOT FOUND")
            HttpEntity(HttpStatus.NOT_FOUND)
        } else {
            LOG.trace("OK")
            HttpEntity(contactDTO)
        }
    }

    @DeleteMapping("/delete")
    @ResponseBody
    fun deleteRequest(@RequestParam(name = "name") name: String): HttpEntity<*> {
        LOG.trace("--- DELETE ---")
        return if (!contactService.deleteContact(name)) {
            LOG.trace("NOT FOUND")
            HttpEntity(HttpStatus.NOT_FOUND)
        } else{
            LOG.trace("OK")
            HttpEntity(HttpStatus.OK)
        }
    }

    @PutMapping("/update")
    @ResponseBody
    fun updateRequest(@RequestParam(name = "name") name: String, @RequestBody contactDTO: ContactDTO): HttpEntity<*> {
        LOG.trace("--- UPDATE ---")
        val done = contactService.updateContact(name, contactDTO)

        return if (!done) {
            LOG.trace("NOT FOUND")
            HttpEntity(HttpStatus.NOT_FOUND)
        } else{
            LOG.trace("OK")
            HttpEntity(HttpStatus.OK)
        }
    }
}