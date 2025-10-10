package com.afr.fms.Maker.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.afr.fms.Maker.entity.Tax;
import com.afr.fms.Maker.entity.TaxFile;
import com.afr.fms.Maker.mapper.TaxFileMapper;
import com.afr.fms.Maker.mapper.TaxableMapper;
import org.springframework.core.io.Resource;



@Service

public class TaxableService {

    @Autowired
    private TaxableMapper taxableMapper;
     
    @Autowired
   private  TaxFileMapper taxFileMapper;

    // @Transactional
    // public String createTax(Tax tax) {

    // System.out.println(tax);
    // String mainGuid = "";

    // mainGuid = generateGuid();
    // tax.setMainGuid(mainGuid);

    // taxableMapper.createTax(tax);

    // System.out.println("Generated mainGuid " + mainGuid);

    // return mainGuid;

    // }

    // @Transactional
    // public String createTaxWithFiles(Tax tax, MultipartFile[] files) throws
    // IOException {

    // System.out.println(tax);
    // TaxFile taxFile= new TaxFile();

    // // 1️⃣ Create DB record
    // String mainGuid = generateGuid();
    // tax.setMainGuid(mainGuid);
    // taxableMapper.createTax(tax); // DB insert happens inside transaction
    // // 2️⃣ Save files only after DB insert
    // if (files != null && files.length > 0) {
    // String uploadDir = Paths.get(System.getProperty("user.dir"),
    // "taxFiles").toString();
    // File dir = new File(uploadDir);
    // if (!dir.exists()) {
    // dir.mkdirs();
    // }

    // for (MultipartFile file : files) {
    // if (!file.isEmpty()) {
    // File destination = new File(dir, file.getOriginalFilename());
    // file.transferTo(destination);

    // taxFile.setFileName(file.getOriginalFilename());
    // taxFile.setExtension(mainGuid);

    // String FileId = generateGuid();
    // taxFile.setId(mainGuid);
    // taxFile.setSupportId(FileId);// foreign key that refernces main Guid

    // // taxableMapper.insertFile(taxFile);

    // System.out.println("Saved file: " + destination.getAbsolutePath());
    // }
    // }
    // }

    // return mainGuid;
    // }

    @Transactional
    public String createTaxWithFiles(Tax tax, MultipartFile[] files) throws IOException {

        // 1️⃣ Generate and set main ID for tax
        String mainGuid = generateGuid();
        tax.setMainGuid(mainGuid);

        String referenceNumber = generateReferenceNumber();
        tax.setReference_number(referenceNumber);
        Long tax_id = taxableMapper.createTax(tax);

        // 2️⃣ If there are files
        if (files != null && files.length > 0 && tax.getTaxFile() != null) {
            String uploadDir = Paths.get(System.getProperty("user.dir"), "taxFiles").toString();
            File dir = new File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            for (int i = 0; i < files.length; i++) {
                MultipartFile file = files[i];
                TaxFile tf = tax.getTaxFile().get(i);
                tf.setTax_id(tax_id);

                // reference to the actual object in tax

                if (!file.isEmpty()) {
                    File destination = new File(dir, file.getOriginalFilename());
                    file.transferTo(destination);

                    // 3️⃣ Generate ID and update the taxFile object
                    String fileId = generateGuid();
                    tf.setId(mainGuid);
                    tf.setSupportId(fileId);
                    tf.setFileName(file.getOriginalFilename());
                    // tf.setExtension(tf.getExtension()); // already set from frontend

                    // ✅ at this point, tax.getTaxFile().get(i) is updated in memory

                    // 4️⃣ Insert into DB
                    taxFileMapper.insertFile(tf);

                    System.out.println("Saved file: " + destination.getAbsolutePath());
                    System.out.println("Updated taxFile object: " + tf);
                }
            }
        }

        System.out.println("Final Tax object with updated taxFile: " + tax);
        return mainGuid;
    }

    public String generateGuid() {
        UUID uuid = UUID.randomUUID(); // Generate a random UUID
        return uuid.toString().toUpperCase(); // Convert to string and make it uppercase
    }

    @Transactional
    public void updateTax(Tax taxable) {

        taxableMapper.createTax(null);

    }

    public Tax fetchTaxById(int id) {

        return taxableMapper.fetchTaxBiID(id);

    }

    public List<Tax> fetchTax(String maker_name) {

        return taxableMapper.fetchTax(maker_name);

    }

    private String generateReferenceNumber() {
        String lastRef = taxableMapper.getLastReferenceNumber();
        if (lastRef == null || lastRef.isEmpty()) {
            return "TAX1";
        }

        // Extract numeric part — e.g., TAX12 → 12
        String numberPart = lastRef.replaceAll("[^0-9]", "");
        int nextNumber = 1;
        try {
            nextNumber = Integer.parseInt(numberPart) + 1;
        } catch (NumberFormatException e) {
            // fallback if number part is invalid
            nextNumber = 1;
        }

        return "TAX" + nextNumber;
    }

    public void deleteTax(String mainGuid) {

        taxableMapper.deleteTaxById(mainGuid);
    }



    
    private final String folderPath = Paths.get(System.getProperty("user.dir"), "taxFiles").toString();

    /**
     * Fetch file from folder by filename
     */
    public File getFileByFileName(String fileName) throws FileNotFoundException {
        File file = new File(folderPath, fileName);
        if (!file.exists() || !file.isFile()) {
            throw new FileNotFoundException("File not found: " + fileName);
        }
        return file;
    }

    /**
     * Optional: Return as Spring Resource for download
     */
    public Resource getFileResource(String fileName) throws FileNotFoundException {
        File file = getFileByFileName(fileName);
        return new FileSystemResource(file);
    }

}
