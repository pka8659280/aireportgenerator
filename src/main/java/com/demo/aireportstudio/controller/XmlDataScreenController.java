package com.demo.aireportstudio.controller;

import com.demo.aireportstudio.model.XmlData;
import com.demo.aireportstudio.services.XmlDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@Controller
public class XmlDataScreenController {

    @Autowired
    private XmlDataService xmlDataService;

    @GetMapping("/xmldata")
    public String xmlDataPage(Model model) {
        model.addAttribute("xmldata", null);
        return "xmldata";
    }

    @GetMapping("/xmldata/{id}")
    public String editXmlDataPage(@PathVariable String id, Model model) {
        Optional<XmlData> xmlData = xmlDataService.findById(id);
        if (xmlData.isPresent()) {
            model.addAttribute("xmldata", xmlData.get());
        } else {
            model.addAttribute("xmldata", null);
        }
        return "xmldata";
    }
}
