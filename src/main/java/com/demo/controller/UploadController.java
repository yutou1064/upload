package com.demo.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UploadController
{
    private static String UPLOADED_FOLDER = "D:\\HouseCall\\uploadFile\\";
    
    @GetMapping("/upload")
    public String index()
    {
        return "upload";
    }
    
    @GetMapping("/multi")
    public String uploadMore()
    {
        return "multiupload";
    }
    
    @GetMapping("/uploadResult")
    public String uploadStatus()
    {
        return "uploadresult";
    }
    
    @PostMapping("/file_upload")
    public String singleFileUpload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes)
    {
        if (file.isEmpty())
        {
            redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
            return "redirect:uploadResult";
        }
        try
        {
            byte[] bytes = file.getBytes();
            Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
            ProcessBuilder proc = new ProcessBuilder("D:\\HouseCall\\HousecallLauncher64.exe",UPLOADED_FOLDER + file.getOriginalFilename());
            proc.start();
            boolean virus = proc.redirectErrorStream();
            
            if(virus) {
            	throw new IOException();
            }else {
            	
            	Files.write(path, bytes);
            	redirectAttributes.addFlashAttribute("message", "Successfully uploaded '" + file.getOriginalFilename() + "'");
            	
            }
            
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return "redirect:/uploadResult";
    }
    
    @PostMapping("/multi_file_upload")
    public String moreFileUpload(@RequestParam("file") MultipartFile[] files, RedirectAttributes redirectAttributes)
    {
        if (files.length == 0)
        {
            redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
            return "redirect:uploadResult";
        }
        for (MultipartFile file : files)
        {
            try
            {
                byte[] bytes = file.getBytes();
                Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
                Files.write(path, bytes);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        redirectAttributes.addFlashAttribute("message", "Successfully uploaded all");
        return "redirect:/uploadResult";
    }
}
