package com.RPMS.controller;


import com.RPMS.controller.contact_strategy.ContactController;
import com.RPMS.model.entity.Email;
import com.RPMS.model.entity.*;
import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.client.ApiClient;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.*;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
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
    private String accountId = "9431335";
    private String accessToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImtpZCI6IjY4MTg1ZmYxLTRlNTEtNGNlOS1hZjFjLTY4OTgxMjIwMzMxNyJ9.eyJUb2tlblR5cGUiOjUsIklzc3VlSW5zdGFudCI6MTU3NDU0MjgyOCwiZXhwIjoxNTc0NTcxNjI4LCJVc2VySWQiOiIxMjU3NTcxNi03Y2I3LTQzMWMtYTIxYi0yNzk2OTI0ODkyZDEiLCJzaXRlaWQiOjEsInNjcCI6WyJzaWduYXR1cmUiLCJjbGljay5tYW5hZ2UiLCJvcmdhbml6YXRpb25fcmVhZCIsImdyb3VwX3JlYWQiLCJwZXJtaXNzaW9uX3JlYWQiLCJ1c2VyX3JlYWQiLCJ1c2VyX3dyaXRlIiwiYWNjb3VudF9yZWFkIiwiZG9tYWluX3JlYWQiLCJpZGVudGl0eV9wcm92aWRlcl9yZWFkIiwiZHRyLnJvb21zLnJlYWQiLCJkdHIucm9vbXMud3JpdGUiLCJkdHIuZG9jdW1lbnRzLnJlYWQiLCJkdHIuZG9jdW1lbnRzLndyaXRlIiwiZHRyLnByb2ZpbGUucmVhZCIsImR0ci5wcm9maWxlLndyaXRlIiwiZHRyLmNvbXBhbnkucmVhZCIsImR0ci5jb21wYW55LndyaXRlIl0sImF1ZCI6ImYwZjI3ZjBlLTg1N2QtNGE3MS1hNGRhLTMyY2VjYWUzYTk3OCIsImlzcyI6Imh0dHBzOi8vYWNjb3VudC1kLmRvY3VzaWduLmNvbS8iLCJzdWIiOiIxMjU3NTcxNi03Y2I3LTQzMWMtYTIxYi0yNzk2OTI0ODkyZDEiLCJhdXRoX3RpbWUiOjE1NzQ1NDI3NjYsInB3aWQiOiJkODlkMWI1OC01N2MwLTRiNTctOGM4Ni05NWNmYzJjY2JkYTkifQ.KhVNfVn4b4eAMHVt0leuP8jOgUFF-iJM7icglx3x9WhlGogqmEN5vS4B_NIt-LrWTOHewJaI47O-1st2kmjKVV1rnZakDhwu25nMnX1CNxJj-ah4QD1xNROFO-i7zgf0YmrvNWIS5sKmqPApG6d3hiThUzX38E21ye7_GY3OAeu7xsnw3TsOEki6Os81hVNfpOnwG6DqMR9S5tIVuDSu4vQSMrKMdH0F0ufJzsHnt4-IuMp87iSIwlhxxasAtW_8LWCeV1mYbPbL2rmj4MgzSOZ0O4Fud2ObueZLRLbgwt_IvNTLHsbTBtPaTnZ_0P6xWSAjHKKtCGz0tkTPMtRHoA";

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
//        em.merge(property);
//        em.getTransaction().commit();
//        em.close();
    }

    public boolean getEnvelopeStatus(Contract contract) {
        try {
            ApiClient apiClient = new ApiClient(basePath);
            apiClient.addDefaultHeader("Authorization", "Bearer " + accessToken);
            EnvelopesApi envelopesApi = new EnvelopesApi(apiClient);
            Envelope results = envelopesApi.getEnvelope(accountId, String.valueOf(contract.getEnvelopeId()));
            return Boolean.getBoolean(results.getStatus());
        } catch (ApiException e) {
            e.printStackTrace();
        }
        return false;
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
            initiateContractSigning("layla.e.arab@gmail.com", property);
        } catch (ApiException ignored) {
        } catch (NoResultException | IOException e) {
            e.printStackTrace();
        }
        em.close();
    }

    public String findNameByEmail(String emailAddress) {
        em = entityManagerFactory.createEntityManager();
        EntityManager em = entityManagerFactory.createEntityManager();
        TypedQuery<Account> findAccount = em.createNamedQuery("Account.findByEmail", Account.class).setParameter("email", emailAddress);
        try {
            String name = findAccount.getSingleResult().getName().toString();
            return name;
        } catch (NoResultException ignored) {
        }
        return null;
    }

    public void initiateContractSigning(String renterEmailAddress, Property property) throws ApiException, IOException {
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
        landlordSignHere1.setXPosition("195");
        landlordSignHere1.setYPosition("147");
        landlordSignHereElements.add(landlordSignHere1);

        List<SignHere> renterSignHereElements = new LinkedList<>();
        SignHere renterSignHere1 = new SignHere();
        renterSignHere1.setDocumentId(String.valueOf(property.getContract().getContractId()));
        renterSignHere1.setPageNumber("1");
        renterSignHere1.setRecipientId("2");
        renterSignHere1.setTabLabel("renterSignHereTab");
        renterSignHere1.setXPosition("195");
        renterSignHere1.setYPosition("147");
        renterSignHereElements.add(renterSignHere1);

        //Text fields
        Text landlordName = new Text();
        landlordName.setDocumentId(String.valueOf(property.getContract().getContractId()));
        landlordName.setTabLabel("firstName");
        landlordName.setPageNumber("1");
        landlordName.setValue(property.getLandlord().getName().toString());
        landlordName.setXPosition("465px");
        landlordName.setYPosition("175px");

        Tabs landlordSignerTabs = new Tabs();
        landlordSignerTabs.setTextTabs(Collections.singletonList(landlordName));
        landlordSignerTabs.setSignHereTabs(landlordSignHereElements);
        landlordSigner.setTabs(landlordSignerTabs);

        Tabs renterSignerTabs = new Tabs();
        renterSignerTabs.setTextTabs(Collections.singletonList(landlordName));
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
        property.getContract().setEnvelopeId(Integer.parseInt(results.getEnvelopeId()));
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
        ContactController.getInstance().performContact(renterURL, renterEmailAddress, "RPMS ACTION REQUIRED: Contract for property at " + property.getAddress());
    }
}


