package com.RPMS.controller;


import com.RPMS.controller.contact_strategy.ContactController;
import com.RPMS.model.entity.Email;
import com.RPMS.model.entity.*;
import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.client.ApiClient;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.*;
import java.util.Base64;


import javax.persistence.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class ContractController {
    /**
     * Allows querying in JPA
     */
    @PersistenceContext
    private EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("RPMS_PU");
    private EntityManager em;

    /**
     * Singleton instance of ContactController
     */
    private static ContractController obj;

    /**
     * ContactController constructor
     */
    private ContractController() {
    }

    private String basePath = "https://demo.docusign.net/restapi";
    private String accountId = "9431335";
    private String accessToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImtpZCI6IjY4MTg1ZmYxLTRlNTEtNGNlOS1hZjFjLTY4OTgxMjIwMzMxNyJ9";

    /**
     * getInstance method for the Singleton pattern
     *
     * @return instance of EmailController
     */
    public static ContractController getInstance() {
        if (obj == null) {
            obj = new ContractController();
        }
        return obj;
    }

    public void createContract(Property property) {
        property.setContract(new Contract());
//        em = entityManagerFactory.createEntityManager();
//        em.getTransaction().begin();
//        em.merge(property);
//        em.getTransaction().commit();
//        em.close();
    }

    public void signEvanContract() {
        Landlord landlord;
        em = entityManagerFactory.createEntityManager();
        EntityManager em = entityManagerFactory.createEntityManager();
        TypedQuery<Account> landlordTypedQuery = em.createNamedQuery("Account.findByEmail", Account.class).setParameter("email", "evan@krul.ca");
        try {
            landlord = (Landlord) landlordTypedQuery.getSingleResult();
            em = entityManagerFactory.createEntityManager();
            TypedQuery<Property> query = em.createNamedQuery("Property.findAllForLandlord", Property.class);
            Property property = query.setParameter("landlord", landlord).getResultList().get(1);
            createContract(property);
            initiateContractSigning(new Email("layla.e.arab@gmail.com"), "RentalAgreement-MonthtoMonth.pdf", property);
        } catch (NoResultException | ApiException | IOException e) {
        }
        em.close();
    }

    public void initiateContractSigning(Email renterEmailAddress, String docPdfName, Property property) throws ApiException, IOException {
        // read file
//        StringBuilder fileContents = new StringBuilder();
//        try {
//            FileReader fileReader = new FileReader(docPdfName);
//            BufferedReader bufferedReader = new BufferedReader(fileReader);
//            String line;
//            while ((line = bufferedReader.readLine()) != null) {
//                fileContents.append(line);
//            }
//            bufferedReader.close();
//            docBase64 = Base64.encodeFromFile(String.valueOf(fileContents));
//        } catch (FileNotFoundException ex) {
//            System.out.println("Unable to open file '" + docPdfName + "'");
//        } catch (IOException ex) {
//            System.out.println("Error reading file '" + docPdfName + "'");
//        }

        byte[] input_file = Files.readAllBytes(Paths.get(docPdfName));
        byte[] encodedBytes = Base64.getEncoder().encode(input_file);
        String encodedString =  new String(encodedBytes);
        // Create the DocuSign document object
        Document document = new Document();
        document.setDocumentBase64(encodedString);
        document.setName("Contract for property at " + property.getAddress()); // can be different from actual file name
        document.setFileExtension("pdf");
        document.setDocumentId(String.valueOf(property.getContract().getId()));  // a label used to reference the doc
        // renter signer
        Signer renterSigner = new Signer();
        renterSigner.setEmail(renterEmailAddress.getEmailAddress());
        renterSigner.recipientId("1");
        // landlord signer
        Signer landlordSigner = new Signer();
        landlordSigner.setEmail(property.getLandlord().getEmail().getEmailAddress());
        landlordSigner.recipientId("1");

        List<Signer> signers = new LinkedList<>();
        signers.add(landlordSigner);
        signers.add(renterSigner);

        List<SignHere> landlordSignHereElements = new LinkedList<>();
        SignHere landlordSignHere1 = new SignHere();
        landlordSignHere1.setDocumentId(String.valueOf(property.getContract().getId()));
        landlordSignHere1.setPageNumber("1");
        landlordSignHere1.setRecipientId("1");
        landlordSignHere1.setTabLabel("landlordSignHereTab");
        landlordSignHere1.setXPosition("195");
        landlordSignHere1.setYPosition("147");
        landlordSignHereElements.add(landlordSignHere1);

        List<SignHere> renterSignHereElements = new LinkedList<>();
        SignHere renterSignHere1 = new SignHere();
        renterSignHere1.setDocumentId(String.valueOf(property.getContract().getId()));
        renterSignHere1.setPageNumber("1");
        renterSignHere1.setRecipientId("2");
        renterSignHere1.setTabLabel("renterSignHereTab");
        renterSignHere1.setXPosition("195");
        renterSignHere1.setYPosition("147");
        renterSignHereElements.add(renterSignHere1);

        Tabs landlordSignerTabs = new Tabs();
        landlordSignerTabs.setSignHereTabs(landlordSignHereElements);
        landlordSigner.setTabs(landlordSignerTabs);

        Tabs renterSignerTabs = new Tabs();
        renterSignerTabs.setSignHereTabs(renterSignHereElements);
        renterSigner.setTabs(renterSignerTabs);

        EnvelopeDefinition envelopeDefinition = new EnvelopeDefinition();
        envelopeDefinition.setEmailSubject("RPMS contract signing for property at:" + property.getAddress());
        envelopeDefinition.setDocuments(Collections.singletonList(document));
        // Add the recipient to the envelope object
        Recipients recipients = new Recipients();
        recipients.setSigners(signers);
        envelopeDefinition.setRecipients(recipients);
        envelopeDefinition.setStatus("sent");

        ApiClient apiClient = new ApiClient(basePath);
        apiClient.addDefaultHeader("Authorization", "Bearer " + accessToken);
        EnvelopesApi envelopesApi = new EnvelopesApi(apiClient);
        EnvelopeSummary results = envelopesApi.createEnvelope(accountId, envelopeDefinition);
        String envelopeId = results.getEnvelopeId();

        RecipientViewRequest landlordViewRequest = new RecipientViewRequest();
        landlordViewRequest.setEmail(renterSigner.getEmail());
        landlordViewRequest.setAuthenticationMethod("none");
        ViewUrl landlordResult = envelopesApi.createRecipientView(accountId, envelopeId, landlordViewRequest);

        RecipientViewRequest renterViewRequest = new RecipientViewRequest();
        renterViewRequest.setEmail(renterSigner.getEmail());
        renterViewRequest.setAuthenticationMethod("none");
        ViewUrl renterResult = envelopesApi.createRecipientView(accountId, envelopeId, renterViewRequest);

        String landlordURL = landlordResult.getUrl();
        String renterURL = renterResult.getUrl();

        ContactController.getInstance().performContact(landlordURL, property.getLandlord().getEmail().getEmailAddress(), "RPMS ACTION REQUIRED: Contract for property at " + property.getAddress());
        ContactController.getInstance().performContact(renterURL, renterEmailAddress.getEmailAddress(), "RPMS ACTION REQUIRED: Contract for property at " + property.getAddress());
    }
}


