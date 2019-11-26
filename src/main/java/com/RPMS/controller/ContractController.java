package com.RPMS.controller;


import com.RPMS.controller.contact_strategy.ContactController;
import com.RPMS.model.entity.Account;
import com.RPMS.model.entity.Contract;
import com.RPMS.model.entity.Landlord;
import com.RPMS.model.entity.Property;
import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.client.ApiClient;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.*;

import javax.persistence.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Base64;
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
    private String accountId = SystemSettingController.getInstance().getSettingByName("accountId").getSettingValue();
    private String accessToken = SystemSettingController.getInstance().getSettingByName("accessToken").getSettingValue();
//    private String accountId = "9482734";
//    private String accessToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImtpZCI6IjY4MTg1ZmYxLTRlNTEtNGNlOS1hZjFjLTY4OTgxMjIwMzMxNyJ9.eyJUb2tlblR5cGUiOjUsIklzc3VlSW5zdGFudCI6MTU3NDc0MzcxMywiZXhwIjoxNTc0NzcyNTEzLCJVc2VySWQiOiI4YTUzNjEwMi1kMDg2LTRjZTctYWI3YS1kNWM0MTY0ODQ5NWQiLCJzaXRlaWQiOjEsInNjcCI6WyJzaWduYXR1cmUiLCJjbGljay5tYW5hZ2UiLCJvcmdhbml6YXRpb25fcmVhZCIsImdyb3VwX3JlYWQiLCJwZXJtaXNzaW9uX3JlYWQiLCJ1c2VyX3JlYWQiLCJ1c2VyX3dyaXRlIiwiYWNjb3VudF9yZWFkIiwiZG9tYWluX3JlYWQiLCJpZGVudGl0eV9wcm92aWRlcl9yZWFkIiwiZHRyLnJvb21zLnJlYWQiLCJkdHIucm9vbXMud3JpdGUiLCJkdHIuZG9jdW1lbnRzLnJlYWQiLCJkdHIuZG9jdW1lbnRzLndyaXRlIiwiZHRyLnByb2ZpbGUucmVhZCIsImR0ci5wcm9maWxlLndyaXRlIiwiZHRyLmNvbXBhbnkucmVhZCIsImR0ci5jb21wYW55LndyaXRlIl0sImF1ZCI6ImYwZjI3ZjBlLTg1N2QtNGE3MS1hNGRhLTMyY2VjYWUzYTk3OCIsImF6cCI6ImYwZjI3ZjBlLTg1N2QtNGE3MS1hNGRhLTMyY2VjYWUzYTk3OCIsImlzcyI6Imh0dHBzOi8vYWNjb3VudC1kLmRvY3VzaWduLmNvbS8iLCJzdWIiOiI4YTUzNjEwMi1kMDg2LTRjZTctYWI3YS1kNWM0MTY0ODQ5NWQiLCJhbXIiOlsiaW50ZXJhY3RpdmUiXSwiYXV0aF90aW1lIjoxNTc0NzQzNzExLCJwd2lkIjoiNmMyZTg2MzctYmJhMC00NWM2LThiZDAtMGY0YTJhMzc5NzU1In0.2Vsp2uovLtwZUENMvXe1tCurCB0EANiVETuJ5NauQAGopH1bQ8LB17X7gIRdyn4mgufm67UuxzhNjUYiV-mn_p4TsH9aypJeIgI3MrGHomcVESqSOKTbcsJXCcbQK7jO1VnFWZk2_GhaWfY4VpzzU3cJRSRF8mbxomAxnPKY7Vw_NLYZEuDVRXczkKhLCBmzpAcaXTj9OUtRuRGrMDQvkDrlHGv-RH5axfj5uU2C6jiZEChHnClTacBJyHEK4nf2ujKmgj0h59shMbMEz9qJvwDr0HztJfDkGCksMphOgHJjUVhWVKe2Yp2YQMtg3EPE5GrelbdHil0_25gBneEpDw";

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
        em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        em.merge(property);
        em.getTransaction().commit();
        em.close();
    }

    public boolean getEnvelopeStatus(Contract contract) {
        try {
            ApiClient apiClient = new ApiClient(basePath);
            apiClient.addDefaultHeader("Authorization", "Bearer " + accessToken);
            EnvelopesApi envelopesApi = new EnvelopesApi(apiClient);
            Envelope results = envelopesApi.getEnvelope(accountId, contract.getEnvelopeId());
            return results.getStatus().equals("completed");
        } catch (ApiException e) {
            e.printStackTrace();
        }
        return false;
    }

//    public void signEvanContract() {
//        Landlord landlord;
//        em = entityManagerFactory.createEntityManager();
//        EntityManager em = entityManagerFactory.createEntityManager();
//        TypedQuery<Account> landlordTypedQuery = em.createNamedQuery("Account.findByEmail", Account.class).setParameter("email", "evan@krul.ca");
//        try {
//            landlord = (Landlord) landlordTypedQuery.getSingleResult();
//            em = entityManagerFactory.createEntityManager();
//            TypedQuery<Property> query = em.createNamedQuery("Property.findAllForLandlord", Property.class);
//            Property property = query.setParameter("landlord", landlord).getResultList().get(1);
//            createContract(property);
//            initiateContractSigning("layla.e.arab@gmail.com", property);
//        } catch (ApiException e) {
//            e.printStackTrace();
//        } catch (NoResultException | IOException e) {
//            e.printStackTrace();
//        }
//        em.close();
//    }

    /**
     * Sends contract for property
     * @param renterEmail
     * @param property
     */
    public void sendContract(String renterEmail, Property property) {
        createContract(property);
        try {
            initiateContractSigning(renterEmail, property);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ApiException e) {
            e.printStackTrace();
        }
    }

    private String findNameByEmail(String emailAddress) {
        em = entityManagerFactory.createEntityManager();
        EntityManager em = entityManagerFactory.createEntityManager();
        TypedQuery<Account> findAccount = em.createNamedQuery("Account.findByEmail", Account.class).setParameter("email", emailAddress);
        try {
            return findAccount.getSingleResult().getName().toString();
        } catch (NoResultException ignored) {
        }
        return null;
    }

    private void initiateContractSigning(String renterEmailAddress, Property property) throws IOException, ApiException {
        byte[] input_file = Files.readAllBytes(Paths.get("RPMS_Rental_Agreement.pdf"));
        byte[] encodedBytes = Base64.getEncoder().encode(input_file);
        String encodedString =  new String(encodedBytes);
        // Create the docusign document object
        Document document = new Document();
        document.setDocumentBase64(encodedString);
        document.setName("Contract for property at " + property.getAddress());
        document.setFileExtension("pdf");
        document.setDocumentId(String.valueOf(property.getContract().getContractId()));
        // renter signer
        String renterName = findNameByEmail(renterEmailAddress);
        if(renterName == null) {
            renterName = "Unregistered Renter";
        }
        Signer renterSigner = new Signer();
        renterSigner.setName(renterName);
        renterSigner.setEmail(renterEmailAddress);
        renterSigner.recipientId("1");
        // landlord signer
        Signer landlordSigner = new Signer();
        landlordSigner.setName(property.getLandlord().getName().toString());
        landlordSigner.setEmail(property.getLandlord().getEmail().getEmailAddress());
        landlordSigner.recipientId("2");

        List<Signer> signers = new LinkedList<>();
        signers.add(landlordSigner);
        signers.add(renterSigner);

        List<SignHere> landlordSignHereElements = new LinkedList<>();
        SignHere landlordSignHere1 = new SignHere();
        landlordSignHere1.setDocumentId(String.valueOf(property.getContract().getContractId()));
        landlordSignHere1.setPageNumber("1");
        landlordSignHere1.setRecipientId("1");
        landlordSignHere1.setTabLabel("landlordSignHereTab");
        landlordSignHere1.setXPosition("93");
        landlordSignHere1.setYPosition("470");
        landlordSignHereElements.add(landlordSignHere1);

        List<SignHere> renterSignHereElements = new LinkedList<>();
        SignHere renterSignHere1 = new SignHere();
        renterSignHere1.setDocumentId(String.valueOf(property.getContract().getContractId()));
        renterSignHere1.setPageNumber("1");
        renterSignHere1.setRecipientId("2");
        renterSignHere1.setTabLabel("renterSignHereTab");
        renterSignHere1.setXPosition("92");
        renterSignHere1.setYPosition("608");
        renterSignHereElements.add(renterSignHere1);

        //Text fields
        Text landlordNameTextField = new Text();
        landlordNameTextField.setDocumentId(String.valueOf(property.getContract().getContractId()));
        landlordNameTextField.setTabLabel("landlord name");
        landlordNameTextField.setPageNumber("1");
        landlordNameTextField.setValue(property.getLandlord().getName().toString());
        landlordNameTextField.setXPosition("344");
        landlordNameTextField.setYPosition("113");

        Text renterNameTextField = new Text();
        renterNameTextField.setDocumentId(String.valueOf(property.getContract().getContractId()));
        renterNameTextField.setTabLabel("renter name");
        renterNameTextField.setPageNumber("1");
        renterNameTextField.setValue(renterName);
        renterNameTextField.setXPosition("127");
        renterNameTextField.setYPosition("144");

        Text dateTextField = new Text();
        LocalDateTime date = LocalDateTime.now();
        dateTextField.setDocumentId(String.valueOf(property.getContract().getContractId()));
        dateTextField.setTabLabel("current date");
        dateTextField.setPageNumber("1");
        dateTextField.setValue(date.getDayOfMonth() + " " + date.getMonth().toString() + " " + date.getYear());
        dateTextField.setXPosition("381");
        dateTextField.setYPosition("145");

        Text addressTextField = new Text();
        addressTextField.setDocumentId(String.valueOf(property.getContract().getContractId()));
        addressTextField.setTabLabel("address");
        addressTextField.setPageNumber("1");
        addressTextField.setValue(property.getAddress().toString());
        addressTextField.setXPosition("97");
        addressTextField.setYPosition("263");

        Text dateStartTextField = new Text();
        dateStartTextField.setDocumentId(String.valueOf(property.getContract().getContractId()));
        dateStartTextField.setTabLabel("start date");
        dateStartTextField.setPageNumber("1");
        dateStartTextField.setValue(date.getMonth().toString() + " " + date.getYear());
        dateStartTextField.setXPosition("266");
        dateStartTextField.setYPosition("320");

        Text rentAmountTextField = new Text();
        rentAmountTextField.setDocumentId(String.valueOf(property.getContract().getContractId()));
        rentAmountTextField.setTabLabel("rent amount");
        rentAmountTextField.setPageNumber("1");
        rentAmountTextField.setValue(String.valueOf(property.getPrice()));
        rentAmountTextField.setXPosition("421");
        rentAmountTextField.setYPosition("396");

        Text dateSignedTextField = new Text();
        dateSignedTextField.setDocumentId(String.valueOf(property.getContract().getContractId()));
        dateSignedTextField.setTabLabel("current date");
        dateSignedTextField.setPageNumber("1");
        dateSignedTextField.setValue(date.getDayOfMonth() + " " + date.getMonth().toString() + " " + date.getYear());
        dateSignedTextField.setXPosition("226");
        dateSignedTextField.setYPosition("510");

        Text UpperRenterNameTextField = new Text();
        UpperRenterNameTextField.setDocumentId(String.valueOf(property.getContract().getContractId()));
        UpperRenterNameTextField.setTabLabel("print renter name");
        UpperRenterNameTextField.setPageNumber("1");
        UpperRenterNameTextField.setValue(renterName.toUpperCase());
        UpperRenterNameTextField.setXPosition("93");
        UpperRenterNameTextField.setYPosition("589");

        List<Text> textTabs = new LinkedList<>();
        textTabs.add(landlordNameTextField);
        textTabs.add(renterNameTextField);
        textTabs.add(dateTextField);
        textTabs.add(addressTextField);
        textTabs.add(dateStartTextField);
        textTabs.add(rentAmountTextField);
        textTabs.add(dateSignedTextField);
        textTabs.add(UpperRenterNameTextField);

        Tabs landlordSignerTabs = new Tabs();
        landlordSignerTabs.setTextTabs(textTabs);
        landlordSignerTabs.setSignHereTabs(landlordSignHereElements);
        landlordSigner.setTabs(landlordSignerTabs);

        Tabs renterSignerTabs = new Tabs();
        renterSignerTabs.setTextTabs(textTabs);
        renterSignerTabs.setSignHereTabs(renterSignHereElements);
        renterSigner.setTabs(renterSignerTabs);

        EnvelopeDefinition envelopeDefinition = new EnvelopeDefinition();
        envelopeDefinition.setEmailSubject("RPMS contract signing for property at " + property.getAddress());
        envelopeDefinition.setDocuments(Collections.singletonList(document));
        // Add the recipients to the envelope object
        Recipients recipients = new Recipients();
        recipients.setSigners(signers);
        envelopeDefinition.setRecipients(recipients);
        envelopeDefinition.setStatus("sent");

        ApiClient apiClient = new ApiClient(basePath);
        apiClient.addDefaultHeader("Authorization", "Bearer " + accessToken);
        EnvelopesApi envelopesApi = new EnvelopesApi(apiClient);
        EnvelopeSummary results = envelopesApi.createEnvelope(accountId, envelopeDefinition);
        property.getContract().setEnvelopeId(results.getEnvelopeId());
        PropertyController.getInstance().saveProperty(property);
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

        ContactController.getInstance().performContact(property.getLandlord().getEmail().getEmailAddress(), landlordURL, property.getLandlord().getEmail().getEmailAddress(), "RPMS ACTION REQUIRED: Contract for property at " + property.getAddress());
        ContactController.getInstance().performContact(renterSigner.getEmail(), renterURL, renterEmailAddress, "RPMS ACTION REQUIRED: Contract for property at " + property.getAddress());
    }
}


