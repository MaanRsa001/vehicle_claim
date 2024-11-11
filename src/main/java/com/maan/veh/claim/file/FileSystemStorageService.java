package com.maan.veh.claim.file;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import com.maan.veh.claim.entity.VcDocumentUploadDetails;
import com.maan.veh.claim.error.Error;
import com.maan.veh.claim.repository.VcDocumentUploadDetailsRepository;
import com.maan.veh.claim.response.CommonRes;

@Service
public class FileSystemStorageService implements StorageService {

	private final Path rootLocation;
	
	@Autowired
	private VcDocumentUploadDetailsRepository documentUploadDetailsRepo;

	@Autowired
	public FileSystemStorageService(StorageProperties properties) {
        
        if(properties.getLocation().trim().length() == 0){
            throw new StorageException("File upload location can not be Empty."); 
        }

		this.rootLocation = Paths.get(properties.getLocation());
	}

	@Override
	public CommonRes store(MultipartFile file, DocumentUploadDetailsReqRes req) {
	    CommonRes response = new CommonRes();  // Initialize the response object

	    try {
	        if (file.isEmpty()) {
	            throw new StorageException("Failed to store empty file.");
	        }

	        Path destinationFile = this.rootLocation.resolve(
	            Paths.get(file.getOriginalFilename()))
	            .normalize().toAbsolutePath();

	        if (!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath())) {
	            // Security check
	            throw new StorageException("Cannot store file outside current directory.");
	        }

	        try (InputStream inputStream = file.getInputStream()) {
	            Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
	        }
	        
	        // Get the file type (extension) in uppercase
	        String originalFileName = file.getOriginalFilename();
	        String fileType = "";
	        if (originalFileName != null && originalFileName.contains(".")) {
	            fileType = originalFileName.substring(originalFileName.lastIndexOf(".") + 1).toUpperCase();
	        }
	        
	        List<VcDocumentUploadDetails> all = documentUploadDetailsRepo.findAllByOrderByDocumentRefDesc();
	        Long id = 0L;
	        if(all != null && all.size()>0) {
	        	id = all.get(0).getDocumentRef() + 1;
	        }

	        // Create and populate VcDocumentUploadDetails entity with values from the request
	        VcDocumentUploadDetails data = new VcDocumentUploadDetails();
	        data.setDocumentRef(id);
	        data.setClaimNo(req.getClaimNo());
	        data.setCompanyId(Integer.parseInt(req.getCompanyId()));
	        data.setDocTypeId(req.getDocTypeId() != null ? Integer.parseInt(req.getDocTypeId()) : null);
	        data.setDocName(req.getDocDesc());
	        data.setFilePathName(destinationFile.toString());
	        data.setUploadedTime(new Date());
	        data.setDescription(req.getDocDesc());
	        data.setFileName(file.getOriginalFilename());
	        data.setUploadType(req.getUploadType());
	        data.setCommonFilePath(req.getCommonFilePath());
	        data.setErrorRes(req.getErrorRes());
	        data.setUploadedBy(req.getUploadedBy());
	        data.setRemarks("Uploaded successfully");
	        data.setFileType(fileType);

	        documentUploadDetailsRepo.save(data);

	        // Set success response details
	        response.setMessage("Document uploaded successfully: " + file.getOriginalFilename());
	        response.setIsError(false);
	        response.setCommonResponse(data);  // Optional: include uploaded document details in response
	        response.setErroCode(0);

	    } catch (Exception e) {
	        // Log and set error response details
	        e.printStackTrace();
	        
	        response.setMessage("Failed to store file: " + e.getMessage());
	        response.setIsError(true);

	        // Optional: add detailed error message information
	        Error error = new Error();
	        error.setCode("500");  // Example error code
	        error.setMessage(""+e.getMessage());
	        response.setErrorMessage(List.of(error));  // Include a list with the error details

	        response.setErroCode(500);  // Standard error code for failed operation
	    }

	    return response;
	}


	@Override
	public Stream<Path> loadAll() {
		try {
			return Files.walk(this.rootLocation, 1)
				.filter(path -> !path.equals(this.rootLocation))
				.map(this.rootLocation::relativize);
		}
		catch (IOException e) {
			throw new StorageException("Failed to read stored files", e);
		}

	}

	@Override
	public Path load(String filename) {
		return rootLocation.resolve(filename);
	}

	@Override
	public Resource loadAsResource(String filename) {
		try {
			Path file = load(filename);
			Resource resource = new UrlResource(file.toUri());
			if (resource.exists() || resource.isReadable()) {
				return resource;
			}
			else {
				throw new StorageFileNotFoundException(
						"Could not read file: " + filename);

			}
		}
		catch (MalformedURLException e) {
			throw new StorageFileNotFoundException("Could not read file: " + filename, e);
		}
	}

	@Override
	public void deleteAll() {
		FileSystemUtils.deleteRecursively(rootLocation.toFile());
	}

	@Override
	public void init() {
		try {
			Files.createDirectories(rootLocation);
		}
		catch (IOException e) {
			throw new StorageException("Could not initialize storage", e);
		}
	}

	@Override
	public CommonRes listUploadedFiles(GetDocListReq req) {
	    CommonRes response = new CommonRes();  // Initialize the response object

	    try {
	        List<DocumentUploadDetailsReqRes> resList = new ArrayList<>();

	        // Fetch documents by claim number
	        List<VcDocumentUploadDetails> docList = documentUploadDetailsRepo.findByClaimNo(req.getClaimNo());

	        // Map each VcDocumentUploadDetails to DocumentUploadDetailsReqRes
	        for (VcDocumentUploadDetails doc : docList) {
	            DocumentUploadDetailsReqRes docRes = DocumentUploadDetailsReqRes.builder()
	                .claimNo(doc.getClaimNo())
	                .documentRef(String.valueOf(doc.getDocumentRef()))
	                .docTypeId(doc.getDocTypeId() != null ? String.valueOf(doc.getDocTypeId()) : null)
	                .docDesc(doc.getDescription())
	                .companyId(String.valueOf(doc.getCompanyId()))
	                .filePathName(doc.getFilePathName())
	                .fileName(doc.getFileName())
	                .uploadType(doc.getUploadType())
	                .commonFilePath(doc.getCommonFilePath())
	                .errorRes(doc.getErrorRes())
	                .imgUrl(getImageUrl(doc.getFilePathName()))
	                .uploadedBy(doc.getUploadedBy())
	                .fileType(doc.getFileType())
	                .build();

	            resList.add(docRes);
	        }

	        // Set success response details
	        response.setMessage("Data retrieved successfully");
	        response.setIsError(false);
	        response.setCommonResponse(resList);  
	        response.setErroCode(0);

	    } catch (Exception e) {
	        // Log and set error response details
	        e.printStackTrace();

	        response.setMessage("Failed to retrieve files: " + e.getMessage());
	        response.setIsError(true);

	        // Add error details to errorMessage list
	        Error error = new Error();
	        error.setCode("500");  // Example error code
	        error.setMessage(e.getMessage());
	        response.setErrorMessage(List.of(error));  // Include a list with the error details

	        response.setErroCode(500);  // Standard error code for failed operation
	    }

	    return response;
	}
	
	public String getImageUrl(String path){

		File file = new File(path);
		String doc;
		if (StringUtils.isNotBlank(path) && new File(path).exists()) {
			byte[] array;
			try {
				array = FileUtils.readFileToByteArray(new File(path));
				

				MultipartFile baseM = new BASE64DecodedMultipartFile(array, file.getName());
				String contenttype = baseM.getContentType();
				String prefix = "data:" + contenttype + ";base64,";

				
				String imgurlen = Base64Utils.encodeToString(array);
				doc = prefix + imgurlen;
				return doc;
				
			} catch (IOException e) {
				e.printStackTrace();
			}

		} else {
			System.out.println("File Is Not Found");
		}
		return null;
	}
	
	@Override
	public CommonRes deleteFile(String claimNo, String documentRef) {
	    CommonRes response = new CommonRes();  // Initialize the response object

	    try {
	        // Find document by claimNo and documentRef
	        Optional<VcDocumentUploadDetails> documentOpt = documentUploadDetailsRepo.findByClaimNoAndDocumentRef(claimNo, Long.valueOf(documentRef));

	        if (documentOpt.isPresent()) {
	            VcDocumentUploadDetails document = documentOpt.get();

	            // Get the file path from the database record
	            String filePath = document.getFilePathName();
	            File file = new File(filePath);

	            // Delete the file from storage
	            if (file.exists() && file.delete()) {
	                // Delete the record from the database
	                documentUploadDetailsRepo.delete(document);

	                // Set success response details
	                response.setMessage("File and record deleted successfully for documentRef: " + documentRef);
	                response.setIsError(false);
	                response.setErroCode(0);
	            } else {
	                // Handle file not found or deletion failure
	                response.setMessage("File could not be found or deleted for documentRef: " + documentRef);
	                response.setIsError(true);
	                response.setErroCode(404);  // Not found error code
	            }
	        } else {
	            // Handle case where document was not found in the database
	            response.setMessage("Document not found for claimNo: " + claimNo + ", documentRef: " + documentRef);
	            response.setIsError(true);
	            response.setErroCode(404);  // Not found error code
	        }
	    } catch (Exception e) {
	        // Log and set error response details
	        e.printStackTrace();

	        response.setMessage("Failed to delete file: " + e.getMessage());
	        response.setIsError(true);

	        // Optional: add detailed error message information
	        Error error = new Error();
	        error.setCode("500");  // Example error code
	        error.setMessage(e.getMessage());
	        response.setErrorMessage(List.of(error));  // Include a list with the error details

	        response.setErroCode(500);  // Standard error code for failed operation
	    }

	    return response;
	}



}
