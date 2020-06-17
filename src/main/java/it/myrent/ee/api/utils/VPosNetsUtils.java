package it.myrent.ee.api.utils;

import it.aessepi.utils.DESEncrypter;
import it.aessepi.utils.beans.FormattedDate;
import it.myrent.ee.api.factory.PrimanotaFactory;
import it.myrent.ee.db.*;
import org.datacontract.schemas._2004._07.bbs_epayment.*;
import org.hibernate.Session;

import javax.xml.bind.JAXBElement;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Shivangani on 1/9/2018.
 */
public class VPosNetsUtils {

    private static final Integer TRANSACTION_NO_RECURRING = 0;
    private static final Integer TRANSACTION_RECURRING_1 = 1;
    private static final Integer TRANSACTION_RECURRING_X = 2;
    private static final int RETURN_OK = 0;
    private static final int RETURN_KO = 1;
    private static SimpleDateFormat formatDate = new SimpleDateFormat("yyyyMMdd");
    private static DecimalFormat formatAmount = new DecimalFormat();
    private static DecimalFormat formatDouble = new DecimalFormat();

    private static AvtaleGiro initializeAvtaleGiro() {
        //Metodo di pagamento Norvegese -> non usato
        return null;
    }

    private static DnBNorDirectPayment initializeDnBNorDirectPayment() {
        //Metodo di pagamento Norvegese -> non usato
        return null;
    }

    private static Fraud initializeFraud() {
        return null;
    }

    private static MicroPayment initializeMicroPayment() {
        return null;
    }

    private static PaymentFacilitatorInformation initializePaymentFacilitatorInformation() {
        return null;
    }

    private static Token initializeToken() {
        return null;
    }

    private static CardInfo initializeCardInfo() {
        //Serve solo se Service Type = C
        return null;
    }

    private static Customer initializeCustomer(Session sxVPOS, MROldContrattoNoleggio contratto, ObjectFactory objectFactory) {
        Customer customer = null;
        String msg = "";
        if (contratto.getCliente() == null) {
            // msg = "The customer of rental agreement is empty!";
            return null;
        }
//        } else if (contratto.getCliente().getId() == null) {
//            JMessage2.showMessage(
//                    this,
//                    bundle.getString("JDIalogAReqPosVirtualeNets.JDialogAReqPosVirtualeNets.msgClienteSenzaID.title"),
//                    bundle.getString("JDIalogAReqPosVirtualeNets.JDialogAReqPosVirtualeNets.msgClienteSenzaID.message"),
//                    5, true);
//            return null;
//        }

        try {
            MROldClienti cliente = (MROldClienti) sxVPOS.get(MROldClienti.class, contratto.getCliente().getId());

            JAXBElement<String> customerNumber = objectFactory.createCustomerCustomerNumber(cliente.getCodiceFiscale() == null ? cliente.getPartitaIva() : cliente.getCodiceFiscale());
            JAXBElement<String> phoneNumber = objectFactory.createCustomerPhoneNumber(cliente.getTelefono1());
            JAXBElement<String> firstName = objectFactory.createCustomerFirstName(cliente.getNome());
            JAXBElement<String> lastName = objectFactory.createCustomerLastName(cliente.getCognome());
            JAXBElement<String> address1 = objectFactory.createCustomerAddress1(cliente.getVia());
            JAXBElement<String> address2 = objectFactory.createCustomerAddress2(null);
            JAXBElement<String> postCode = objectFactory.createCustomerPostcode(cliente.getCap());
            JAXBElement<String> town = objectFactory.createCustomerTown(cliente.getCitta());
            String nazione = null;
//            if (cliente.getNazione() != null) {
//                if (cliente.getNazione().equals("ITALIA")) {
//                    nazione = "ITALY";
//                } else {
                    nazione = cliente.getNazione();
//                }
//            }
            JAXBElement<String> country = objectFactory.createCustomerCountry(RicercheUtils.getNazioneISO(sxVPOS, nazione));
            JAXBElement<String> socialSecurityNumber = objectFactory.createCustomerSocialSecurityNumber(null);
            JAXBElement<String> companyName = objectFactory.createCustomerCompanyName(cliente.getRagioneSociale());
            JAXBElement<String> companyRegistrationNumber = objectFactory.createCustomerCompanyRegistrationNumber(cliente.getPartitaIva());
            JAXBElement<String> notificationMode = objectFactory.createCustomerNotificationMode(null);
            JAXBElement<String> customerIsCompany = objectFactory.createCustomerCustomerIsCompany(cliente.getPersonaFisica().equals(Boolean.TRUE) ? "false" : "true");
            JAXBElement<String> accountNumber = objectFactory.createCustomerAccountNumber(null);
            JAXBElement<String> gender;
            if (cliente.getSesso() != null) {
                gender = objectFactory.createCustomerGender(cliente.getSesso().equals(MROldClienti.SESSO_MASCHILE) ? "M" : "F");
            } else {
                gender = null;
            }
            JAXBElement<String> dateOfBirth = objectFactory.createCustomerDateOfBirth(cliente.getDataNascita() != null ? formatDate.format(cliente.getDataNascita()) : null);

            customer = new Customer();
            customer.setCustomerNumber(customerNumber);
            //customer.setPhoneNumber(phoneNumber);
            customer.setFirstName(firstName);
            customer.setLastName(lastName);
            customer.setAddress1(address1);
            customer.setAddress2(address2);
            customer.setPostcode(postCode);
            customer.setTown(town);
            customer.setCountry(country);
            customer.setSocialSecurityNumber(socialSecurityNumber);
            customer.setCompanyName(companyName);
            customer.setCompanyRegistrationNumber(companyRegistrationNumber);
            customer.setNotificationMode(notificationMode);
            customer.setCustomerIsCompany(customerIsCompany);
            customer.setAccountNumber(accountNumber);
            customer.setGender(gender);
            customer.setDateOfBirth(dateOfBirth);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return customer;
    }

    private static Environment initializeEnvironment(ObjectFactory objectFactory) {
        Environment environment = null;

        try {
            //DesktopApi.EnumOS operatingSystem = getOs();
            //String os = operatingSystem.toString();

            String language = null;
            if (System.getProperty("user.language") != null && System.getProperty("user.language").length() > 0) {
                language = System.getProperty("user.language");
            }

            String webServicePlatform = "DOTNET20";

            JAXBElement<String> languageJAXB = objectFactory.createEnvironmentLanguage(language);
            JAXBElement<String> osJAXB = objectFactory.createEnvironmentOS("windows");
            JAXBElement<String> webServicePlatformJAXB = objectFactory.createEnvironmentWebServicePlatform(webServicePlatform);

            environment = new Environment();
            environment.setLanguage(languageJAXB);
            environment.setOS(osJAXB);
            environment.setWebServicePlatform(webServicePlatformJAXB);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return environment;
    }

    private static Order initializeOrder(Session sxVPOS, MROldContrattoNoleggio contratto, ObjectFactory objectFactory, Double transactionAmount, String currency) {
        Order order = null;

        try {
            JAXBElement<String> amount = objectFactory.createOrderAmount(formatAmount.format(transactionAmount).replace(".", "").replace(",", ""));
            JAXBElement<String> currencyCode = objectFactory.createOrderCurrencyCode(currency);
            JAXBElement<String> deliveryDate = objectFactory.createOrderDeliveryDate(null);
            JAXBElement<String> force3DSecure = objectFactory.createOrderForce3DSecure(null);
            ArrayOfItem goods = initializeGoods(sxVPOS, contratto);
            JAXBElement<ArrayOfItem> goodsJAXB = objectFactory.createOrderGoods(goods);
            JAXBElement<String> orderNumber = objectFactory.createOrderOrderNumber(contratto.getPrefisso() + "_" + contratto.getNumero());
            JAXBElement<String> updateStoredPaymentInfo = objectFactory.createOrderUpdateStoredPaymentInfo("false");

            order = new Order();
            order.setAmount(amount);
            order.setCurrencyCode(currencyCode);
            order.setDeliveryDate(deliveryDate);
            order.setForce3DSecure(force3DSecure);
            order.setGoods(goodsJAXB);
            order.setOrderNumber(orderNumber);
            order.setUpdateStoredPaymentInfo(updateStoredPaymentInfo);
            order.setValidateCustomer(null);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return order;
    }

    private static ArrayOfItem initializeGoods(Session sxVPOS, MROldContrattoNoleggio contratto) {
        ArrayOfItem goods = new ArrayOfItem();
        return null;

        /*
        try {
            if (contratto.getDocumentiFiscali() != null && !contratto.getDocumentiFiscali().isEmpty()) {

                ArrayList arrayListDocumentiFiscali = new ArrayList<>(Arrays.asList(contratto.getDocumentiFiscali().toArray()));
                for (Object fatturaObject : arrayListDocumentiFiscali) {
                    DocumentoFiscale fattura = (DocumentoFiscale) fatturaObject;
                    if (fattura == null) {
                        //TO DO -> spara avviso di fattura vuota
                        return null;
                    } else if (fattura.getFatturaRighe() == null) {
                        //TO DO -> spara avviso di fattura senza righe
                        return null;
                    } else if (fattura.getId() == null) {
                        //TO DO -> spara avviso di fattura senza id
                        return null;
                    }

                    JAXBElement<String> amountFattura = objectFactory.createItemAmount(formatAmount.format(fattura.getTotaleFattura()).replace(",", "").replace(".", "")); //rigaFattura.getTotaleRiga()
                    JAXBElement<String> articleNumber = objectFactory.createItemArticleNumber(fattura.getId().toString());
                    JAXBElement<String> title = objectFactory.createItemTitle(null);
                    JAXBElement<String> unitCode = objectFactory.createItemUnitCode(null);

                    Item item = new Item();
                    item.setAmount(amountFattura);
                    item.setArticleNumber(articleNumber);
                    item.setDiscount(null);
                    item.setHandling(null);
                    item.setIsVatIncluded(true);
                    item.setQuantity(1);
                    item.setShipping(null);
                    item.setTitle(title);
                    item.setVAT(null);
                    item.setUnitCode(unitCode);

                    goods.getItem().add(item);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return goods;
         */
    }

    private static Recurring initializeRecurring(Integer transactionType, Session sx, ObjectFactory objectFactory, MROldNetsTransazione selectedNetsTransazione, String panHashChoosen) {
        Recurring recurring = null;

        try {
            if (!transactionType.equals(TRANSACTION_NO_RECURRING)) {
                JAXBElement<String> recurringType = objectFactory.createRecurringType("R");

                if (transactionType.equals(TRANSACTION_RECURRING_1)) {
                    JAXBElement<String> recurringFrequency = objectFactory.createRecurringFrequency("0");
                    Calendar cal = Calendar.getInstance();
                    cal.add(Calendar.MONTH, +9);
                    JAXBElement<String> recurringExpiryDate = objectFactory.createRecurringExpiryDate(formatDate.format(cal.getTime()));

                    recurring = new Recurring();
                    recurring.setType(recurringType);
                    recurring.setFrequency(recurringFrequency);
                    recurring.setExpiryDate(recurringExpiryDate);

                    selectedNetsTransazione.setRecurringFrequency(0);
                    selectedNetsTransazione.setRecurringExpiryDate(cal.getTime());
                    selectedNetsTransazione.setPanHash(null);
                } else if (transactionType.equals(TRANSACTION_RECURRING_X)) {
                    JAXBElement<String> recurringPanHash = objectFactory.createRecurringPanHash(panHashChoosen);

                    recurring = new Recurring();
                    recurring.setType(recurringType);
                    recurring.setPanHash(recurringPanHash);
                    DESEncrypter desEncrypter = new DESEncrypter("utenti_myrent");
                    String encryptStr = desEncrypter.encrypt(panHashChoosen);
                    selectedNetsTransazione.setPanHash(encryptStr);
                } else {
                    System.out.println("PROBABILE ERRORE DI SCELTE!");
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return recurring;
    }

    private static Terminal initializeTerminal(MROldContrattoNoleggio contratto, ObjectFactory objectFactory, MROldNetsAnagrafica actualNetsAnagrafica) {
        Terminal terminal = null;

        try {
            String orderDescription = contratto.toString();
            String language = "en_GB";
            if (System.getProperty("user.language") != null && System.getProperty("user.language").length() > 0) {
                String languageSystem = System.getProperty("user.language");

                if (languageSystem.toLowerCase().equals("it")) {
                    language = "it_IT";
                } else if (languageSystem.toLowerCase().equals("sv")) {
                    language = "sv_SE";
                }
            }
            String redirectUrl = actualNetsAnagrafica.getNotifyUrl(); //"http://localhost/OK";
            String redirectUrlError = actualNetsAnagrafica.getErrorUrl(); //"http://localhost/KO";
            ArrayList<PaymentMethodAction> paymentMethodActionList = null;
            String paymentMethodList = null;
            String feeList = null;
            String vat = formatAmount.format(contratto.getNoleggio() - contratto.getNoleggioNoVat()).replace(".", "");
            String autoAuth = "false";
            String autoSale = "false";
            String design = null;
            ArrayList<KeyValuePair> templateData = null;
            String singlePage = "true";
            String layout = null;
            String walletProviderId = null;
            String financialOperation = null;

            JAXBElement<String> orderDescriptionJAXB = objectFactory.createTerminalOrderDescription(orderDescription);
            JAXBElement<String> languageJAXB = objectFactory.createTerminalLanguage(language);
            JAXBElement<String> redirectUrlJAXB = objectFactory.createTerminalRedirectUrl(redirectUrl);
            JAXBElement<String> redirectOnErrorJAXB = objectFactory.createTerminalRedirectOnError(redirectUrlError);
            JAXBElement<ArrayOfPaymentMethodAction> paymentMethodActionListJAXB = objectFactory.createTerminalPaymentMethodActionList(null);
            JAXBElement<String> paymentMethodListJAXB = objectFactory.createTerminalPaymentMethodList(paymentMethodList);
            JAXBElement<String> feeListJAXB = objectFactory.createTerminalFeeList(feeList);
            JAXBElement<String> vatJAXB = objectFactory.createTerminalVat(vat);
            JAXBElement<String> autoAuthJAXB = objectFactory.createTerminalAutoAuth(autoAuth);
            JAXBElement<String> autoSaleJAXB = objectFactory.createTerminalAutoSale(autoSale);
            JAXBElement<String> designJAXB = objectFactory.createTerminalDesign(design);
            JAXBElement<ArrayOfKeyValuePair> templateDataJAXB = objectFactory.createTerminalTemplateData(null);
            JAXBElement<String> singlePageJAXB = objectFactory.createTerminalSinglePage(singlePage);
            JAXBElement<String> layoutJAXB = objectFactory.createTerminalLayout(layout);
            JAXBElement<String> walletProviderIdJAXB = objectFactory.createTerminalWalletProviderId(walletProviderId);
            JAXBElement<String> financialOperationJAXB = objectFactory.createTerminalFinancialOperation(financialOperation);

            terminal = new Terminal();
            terminal.setOrderDescription(orderDescriptionJAXB);
            terminal.setLanguage(languageJAXB);
            terminal.setRedirectUrl(redirectUrlJAXB);
            terminal.setRedirectOnError(redirectOnErrorJAXB);
            terminal.setPaymentMethodActionList(paymentMethodActionListJAXB);
            terminal.setPaymentMethodList(paymentMethodListJAXB);
            terminal.setFeeList(feeListJAXB);
            terminal.setVat(vatJAXB);
            terminal.setAutoAuth(autoAuthJAXB);
            terminal.setAutoSale(autoSaleJAXB);
            terminal.setDesign(designJAXB);
            terminal.setTemplateData(templateDataJAXB);
            terminal.setSinglePage(singlePageJAXB);
            terminal.setLayout(layoutJAXB);
            terminal.setWalletProviderId(walletProviderIdJAXB);
            terminal.setFinancialOperation(financialOperationJAXB);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return terminal;
    }

    public static RegisterRequest initializeRegisterRequest(Session sx, MROldContrattoNoleggio contrattoNoleggio, Integer transactionType, Double transactionAmount, String currency, MROldNetsTransazione selectedNetsTransazione, String panHashChoosen, MROldNetsAnagrafica netsAnagrafica) {

        //Map result = new HashMap();
        RegisterRequest request = new RegisterRequest();
        ObjectFactory objectFactory = new ObjectFactory();
        request.setFraud(objectFactory.createFraud(initializeFraud()));
        request.setAcquirerInfo(null);
        request.setAvtaleGiro(objectFactory.createAvtaleGiro(initializeAvtaleGiro()));
        request.setCardInfo(objectFactory.createCardInfo(initializeCardInfo()));
        request.setCustomer(objectFactory.createCustomer(initializeCustomer(sx, contrattoNoleggio, objectFactory)));
        request.setDescription(objectFactory.createRegisterRequestDescription("R.A. number " + contrattoNoleggio.getPrefisso() + " " + contrattoNoleggio.getNumero()));
        request.setDnBNorDirectPayment(objectFactory.createDnBNorDirectPayment(initializeDnBNorDirectPayment()));
        request.setEnvironment(objectFactory.createEnvironment(initializeEnvironment(objectFactory)));
        request.setMicroPayment(objectFactory.createMicroPayment(initializeMicroPayment()));
        request.setOrder(objectFactory.createOrder(initializeOrder(sx, contrattoNoleggio, objectFactory, transactionAmount, currency)));
        request.setPaymentFacilitator(objectFactory.createPaymentFacilitatorInformation(initializePaymentFacilitatorInformation()));
        request.setRecurring(objectFactory.createRecurring(initializeRecurring(transactionType, sx, objectFactory, selectedNetsTransazione, panHashChoosen)));
        if (transactionType.equals(TRANSACTION_RECURRING_X)) {
            request.setServiceType(objectFactory.createRegisterRequestServiceType("C"));
        } else {
            request.setServiceType(objectFactory.createRegisterRequestServiceType("B"));
            request.setTerminal(objectFactory.createTerminal(initializeTerminal(contrattoNoleggio, objectFactory, netsAnagrafica)));
        }
        request.setToken(objectFactory.createToken(initializeToken()));
        request.setTransactionId(objectFactory.createRegisterRequestTransactionId(null));
        request.setTransactionReconRef(objectFactory.createRegisterRequestTransactionReconRef(null));
        return request;

    }

    public static QueryRequest initializeQueryRequest(String transactionId) {
        QueryRequest queryRequest = null;
        try {
            ObjectFactory objectFactory = new ObjectFactory();
            queryRequest = new QueryRequest();
            DESEncrypter desEncrypter = new DESEncrypter("utenti_myrent");
            queryRequest.setTransactionId(objectFactory.createQueryRequestTransactionId(desEncrypter.decrypt(transactionId)));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return queryRequest;
    }

    public static ProcessRequest initializeProcessRequestSale(Session sxVPOS, MROldContrattoNoleggio contratto, String transactionId) {
        ProcessRequest request = null;

        try {
            request = new ProcessRequest();
            ObjectFactory objectFactory = new ObjectFactory();
            DESEncrypter desEncrypter = new DESEncrypter("utenti_myrent");
            String idTransazione = desEncrypter.decrypt(transactionId);

            request.setOperation(objectFactory.createProcessRequestOperation("SALE"));
            request.setTransactionId(objectFactory.createProcessRequestTransactionId(idTransazione));
            request.setDescription(objectFactory.createProcessRequestDescription("Sale transaction " + idTransazione));
            request.setTransactionAmount(objectFactory.createProcessRequestTransactionAmount(formatAmount.format(contratto.getNoleggio()).replace(",", "").replace(".", "")));
            request.setTransactionReconRef(objectFactory.createProcessRequestTransactionReconRef(null));
            request.setGoods(objectFactory.createProcessRequestGoods(initializeGoods(sxVPOS, contratto)));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return request;
    }

    public static Map checkProcessResponseSale(Session sx, ProcessResponse response, Double transactionAmt, MROldNetsTransazione selectedNetsTransazione,
                                               MROldContrattoNoleggio contrattoNoleggio, MROldPagamento pagamento, User aUser) {
        Map result = new HashMap();
        try {
            if (response != null && response.getTransactionId() != null) {
                MROldNetsOperationCall operationSale = new MROldNetsOperationCall();
                operationSale.setAmount(transactionAmt);
                Calendar cal = Calendar.getInstance();
                operationSale.setDate(cal.getTime());
                operationSale.setTransazioneNets(selectedNetsTransazione);
                operationSale.setType(MROldNetsOperationCall.SALE_CALL);
                if (response.getResponseCode().getValue().equals("OK")) {
                    MROldPrimanota primanotaIncasso = PrimanotaFactory.creaPrimanotaIncassoCliente(
                            sx,
                            aUser.getSedeOperativa(), //sede
                            contrattoNoleggio,
                            pagamento,
                            null, //garanzia
                            transactionAmt,
                            aUser);
                    MROldNumerazione numerazionePrimenote = NumerazioniUtils.getNumerazione(sx, MROldNumerazione.PRIMENOTE, aUser);
                    Integer numeroRegistrazionePrimenote = NumerazioniUtils.aggiornaProgressivo(sx, numerazionePrimenote, FormattedDate.annoCorrente(), 1);
                    primanotaIncasso.setNumerazione(numerazionePrimenote);
                    primanotaIncasso.setNumeroRegistrazione(numeroRegistrazionePrimenote);
                    sx.saveOrUpdate(primanotaIncasso);
                    result.put("transMsg", "SALE OK");
                    result.put("transMsgColor", "Green");
                    result.put("checkBtn", "N");
                    result.put("saleBtn", "N");
                    result.put("authBtn", "N");
                    result.put("captureBtn", "N");
                    result.put("cancelBtn", "N");
                    result.put("creditBtn", "Y");
                    result.put("isAuthAmt", "Y");
                    result.put("authAmt1", "0.0");
                    result.put("isCaptAmt", "N");
                    result.put("captAmt1", transactionAmt);
                    result.put("captAmt", transactionAmt);
                    result.put("isCreditAmt", "Y");
                    result.put("creditAmt1", transactionAmt);
                } else {
                    result.put("transMsg", "SALE KO: " + response.getResponseText().getValue());
                    result.put("transMsgColor", "Red");
                    operationSale.setError("RESPONSE Code: " + response.getResponseCode().getValue() + " - "
                            + "RESPONSE Source: " + response.getResponseSource().getValue() + " - "
                            + "RESPONSE Text: " + response.getResponseText().getValue());
                }
                sx.save(operationSale);
                selectedNetsTransazione.setState(MROldNetsTransazione.STATE_SALE);
                selectedNetsTransazione.setLastUpdateDate(cal.getTime());
                sx.update(selectedNetsTransazione);
                sx.flush();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public static ProcessRequest initializeProcessRequestAuthorize(Session sx, MROldContrattoNoleggio contrattoNoleggio, String transId, Double authAmt) {
        ProcessRequest request = null;

        try {
            request = new ProcessRequest();
            DESEncrypter desEncrypter = new DESEncrypter("utenti_myrent");
            String idTransazione = desEncrypter.decrypt(transId);
            ObjectFactory objectFactory = new ObjectFactory();
            request.setOperation(objectFactory.createProcessRequestOperation("AUTH"));
            request.setTransactionId(objectFactory.createProcessRequestTransactionId(idTransazione));
            request.setDescription(objectFactory.createProcessRequestDescription("Authorize transaction " + idTransazione));
            request.setTransactionAmount(objectFactory.createProcessRequestTransactionAmount(formatAmount.format(authAmt).replace(",", "").replace(".", "")));
            request.setTransactionReconRef(objectFactory.createProcessRequestTransactionReconRef(null));
            request.setGoods(objectFactory.createProcessRequestGoods(initializeGoods(sx, contrattoNoleggio)));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return request;
    }

    public static Map checkProcessResponseAuthorize(Session sx, ProcessResponse response, Double authAmt, MROldNetsTransazione selectedNetsTransazione) {
        Map result = new HashMap();
        if (response != null && response.getTransactionId() != null) {
            if (response.getResponseCode().getValue().equals("OK")) {
                result.put("transMsg", "AUTHORIZE OK");
                result.put("transMsgColor", "Green");
//                result.put("checkBtn", "N");
//
//
//                result.put("creditBtn", "N");
//
//
//                result.put("isCreditAmt", "N");
//                result.put("creditAmt1", "0.0");
//                result.put("authAmt", authAmt);
                result.put("captureBtn", "Y");
                result.put("cancelBtn", "Y");
                result.put("saleBtn", "N");
                result.put("authBtn", "N");

                result.put("isCaptAmt", "Y");
                result.put("captAmt1", authAmt);

                result.put("isAuthAmt", "N");
                result.put("authAmt", authAmt);

                selectedNetsTransazione.setState(MROldNetsTransazione.STATE_AUTHORIZED);
                Calendar cal = Calendar.getInstance();
                selectedNetsTransazione.setLastUpdateDate(cal.getTime());
                selectedNetsTransazione.setAmountAuth(authAmt);
                sx.update(selectedNetsTransazione);
            } else {
                System.out.println("RESPONSE Code: " + response.getResponseCode().getValue());
                System.out.println("RESPONSE Source: " + response.getResponseSource().getValue());
                System.out.println("RESPONSE Text: " + response.getResponseText().getValue());
                result.put("transMsg", "AUTHORIZE KO: " + response.getResponseText().getValue());
                result.put("transMsgColor", "Red");
                System.out.println("Authorize NOT OK!");
            }
        }
        return result;
    }

    public static ProcessRequest initializeProcessRequestCapture(Session sx, MROldContrattoNoleggio contrattoNoleggio, Double doubleCapture, String transId) {
        ProcessRequest request = null;

        try {
            request = new ProcessRequest();
            //Double doubleCapture = doublePanelCaptureAmount.getTextFieldValue();
            DESEncrypter desEncrypter = new DESEncrypter("utenti_myrent");
            String idTransazione = desEncrypter.decrypt(transId);
            ObjectFactory objectFactory = new ObjectFactory();
            request.setOperation(objectFactory.createProcessRequestOperation("CAPTURE"));
            request.setTransactionId(objectFactory.createProcessRequestTransactionId(idTransazione));
            request.setDescription(objectFactory.createProcessRequestDescription("Capture transaction " + idTransazione));
            request.setTransactionAmount(objectFactory.createProcessRequestTransactionAmount(formatAmount.format(doubleCapture).replace(",", "").replace(".", "")));
            request.setTransactionReconRef(objectFactory.createProcessRequestTransactionReconRef(null));
            request.setGoods(objectFactory.createProcessRequestGoods(initializeGoods(sx, contrattoNoleggio)));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return request;

    }

    public static Map checkProcessResponseCapture(Session sx, ProcessResponse response, Double doubleCapture, MROldNetsTransazione selectedNetsTransazione, Double authdAmt,
                                                  MROldContrattoNoleggio contrattoNoleggio, User aUser, MROldPagamento pagamento, Double creditAmt, Double capturedAmt) {
        Map result = new HashMap();
        if (response != null && response.getTransactionId() != null) {
            MROldNetsOperationCall operationCapture = new MROldNetsOperationCall();
            operationCapture.setAmount(doubleCapture);
            Calendar cal = Calendar.getInstance();
            operationCapture.setDate(cal.getTime());
            operationCapture.setTransazioneNets(selectedNetsTransazione);
            operationCapture.setType(MROldNetsOperationCall.CAPTURE_CALL);
            if (response.getResponseCode().getValue().equals("OK")) {
                MROldPrimanota primanotaIncasso = PrimanotaFactory.creaPrimanotaIncassoCliente(
                        sx,
                        aUser.getSedeOperativa(), //sede
                        contrattoNoleggio,
                        pagamento,
                        null, //garanzia
                        doubleCapture,
                        aUser);

                MROldNumerazione numerazionePrimenote = NumerazioniUtils.getNumerazione(sx, MROldNumerazione.PRIMENOTE, aUser);
                Integer numeroRegistrazionePrimenote = NumerazioniUtils.aggiornaProgressivo(sx, numerazionePrimenote, FormattedDate.annoCorrente(), 1);
                primanotaIncasso.setNumerazione(numerazionePrimenote);
                primanotaIncasso.setNumeroRegistrazione(numeroRegistrazionePrimenote);
                sx.saveOrUpdate(primanotaIncasso);
                result.put("transMsg", "CAPTURE OK");
                result.put("transMsgColor", "Green");
//                result.put("checkBtn", "N");
//                result.put("saleBtn", "N");
//                result.put("authBtn", "N");
//                result.put("captureBtn", "N");
                result.put("cancelBtn", "N");
                result.put("creditBtn", "Y");
//                result.put("isAuthAmt", "Y");
//                result.put("authAmt1", "0.0");
//                result.put("isCaptAmt", "N");
                result.put("isCreditAmt", "Y");
                result.put("creditAmt1", doubleCapture + creditAmt);
                result.put("captAmt", doubleCapture + capturedAmt);
                result.put("captAmt1", authdAmt - (doubleCapture + capturedAmt));

                selectedNetsTransazione.setState(MROldNetsTransazione.STATE_CAPTUR);
                if (authdAmt - (doubleCapture + capturedAmt) == 0) {
                    result.put("isCaptAmt", "N");
                    result.put("captureBtn", "N");
                    selectedNetsTransazione.setState(MROldNetsTransazione.STATE_SALE);
                }
            } else {
                System.out.println("RESPONSE Code: " + response.getResponseCode().getValue());
                System.out.println("RESPONSE Source: " + response.getResponseSource().getValue());
                System.out.println("RESPONSE Text: " + response.getResponseText().getValue());
                result.put("transMsg", "CAPTURE KO: " + response.getResponseText().getValue());
                result.put("transMsgColor", "Red");
                System.out.println("Capture NOT OK!");

                operationCapture.setError("RESPONSE Code: " + response.getResponseCode().getValue() + " - "
                        + "RESPONSE Source: " + response.getResponseSource().getValue() + " - "
                        + "RESPONSE Text: " + response.getResponseText().getValue());
            }
            sx.save(operationCapture);
            selectedNetsTransazione.setLastUpdateDate(cal.getTime());
            sx.update(selectedNetsTransazione);
            sx.flush();
        }
        return result;
    }

    public static ProcessRequest initializeProcessRequestAnnull(Session sxVPOS, MROldContrattoNoleggio contratto, String transId) {
        ProcessRequest request = null;

        try {
            request = new ProcessRequest();
            DESEncrypter desEncrypter = new DESEncrypter("utenti_myrent");
            String idTransazione = desEncrypter.decrypt(transId);
            ObjectFactory objectFactory = new ObjectFactory();
            request.setOperation(objectFactory.createProcessRequestOperation("ANNUL"));
            request.setTransactionId(objectFactory.createProcessRequestTransactionId(idTransazione));
            request.setDescription(objectFactory.createProcessRequestDescription("Annul transaction " + idTransazione));
            request.setTransactionAmount(objectFactory.createProcessRequestTransactionAmount(formatAmount.format(contratto.getNoleggio()).replace(",", "").replace(".", "")));
            request.setTransactionReconRef(objectFactory.createProcessRequestTransactionReconRef(null));
            request.setGoods(objectFactory.createProcessRequestGoods(initializeGoods(sxVPOS, contratto)));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return request;
    }

    public static Map checkProcessResponseAnnull(Session sxVPOS, ProcessResponse response, MROldNetsTransazione selectedNetsTransazione) {
        Map result = new HashMap();

        if (response != null && response.getTransactionId() != null) {

            try {

                if (response.getResponseCode().getValue().equals("OK")) {
                    result.put("transMsg", "ANNUL OK");
                    result.put("transMsgColor", "Green");
                    result.put("transId", "");
                    //initializeButtons();
                    System.out.println("Annul OK!");

                    selectedNetsTransazione.setState(MROldNetsTransazione.STATE_ANNULL);
                    Calendar cal = Calendar.getInstance();
                    selectedNetsTransazione.setLastUpdateDate(cal.getTime());
                    sxVPOS.update(selectedNetsTransazione);
                    sxVPOS.flush();
                } else {
                    System.out.println("RESPONSE Code: " + response.getResponseCode().getValue());
                    System.out.println("RESPONSE Source: " + response.getResponseSource().getValue());
                    System.out.println("RESPONSE Text: " + response.getResponseText().getValue());
                    result.put("transMsg", "ANNUL KO: " + response.getResponseText().getValue());
                    result.put("transMsgColor", "Red");
                    System.out.println("Annul NOT OK!");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

    public static ProcessRequest initializeProcessRequestCredit(Session sxVPOS, MROldContrattoNoleggio contratto, String transId, Double creditAmount) {
        ProcessRequest request = null;

        try {
            request = new ProcessRequest();
            DESEncrypter desEncrypter = new DESEncrypter("utenti_myrent");
            String idTransazione = desEncrypter.decrypt(transId);
            ObjectFactory objectFactory = new ObjectFactory();
            request.setOperation(objectFactory.createProcessRequestOperation("CREDIT"));
            request.setTransactionId(objectFactory.createProcessRequestTransactionId(idTransazione));
            request.setDescription(objectFactory.createProcessRequestDescription("Credit transaction " + idTransazione));
            request.setTransactionAmount(objectFactory.createProcessRequestTransactionAmount(formatAmount.format(creditAmount).replace(",", "").replace(".", "")));
            request.setTransactionReconRef(objectFactory.createProcessRequestTransactionReconRef(null));
            request.setGoods(objectFactory.createProcessRequestGoods(initializeGoods(sxVPOS, contratto)));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return request;
    }

    public static Map checkProcessResponseCredit(Session sxVPOS, ProcessResponse response, Double creditAmount, MROldNetsTransazione selectedNetsTransazione,
                                           Double captdAmount, User aUser, MROldContrattoNoleggio contratto, MROldPagamento pagamento, Double creditedAmt, Double creditLimit) {

        Map result = new HashMap();
        if (response != null && response.getTransactionId() != null) {

            try {
                MROldNetsOperationCall operationCredit = new MROldNetsOperationCall();
                operationCredit.setAmount(creditAmount);
                Calendar cal = Calendar.getInstance();
                operationCredit.setDate(cal.getTime());
                operationCredit.setTransazioneNets(selectedNetsTransazione);
                operationCredit.setType(MROldNetsOperationCall.CREDIT_CALL);

                if (response.getResponseCode().getValue().equals("OK")) {
                    MROldPrimanota primanotaRimborso = PrimanotaFactory.creaPrimanotaRimborsoCliente(
                            sxVPOS,
                            aUser.getSedeOperativa(), //sede
                            contratto,
                            pagamento,
                            creditAmount,
                            aUser);

                    MROldNumerazione numerazionePrimenote = NumerazioniUtils.getNumerazione(sxVPOS, MROldNumerazione.PRIMENOTE, aUser);
                    Integer numeroRegistrazionePrimenote = NumerazioniUtils.aggiornaProgressivo(sxVPOS, numerazionePrimenote, FormattedDate.annoCorrente(), 1);
                    primanotaRimborso.setNumerazione(numerazionePrimenote);
                    primanotaRimborso.setNumeroRegistrazione(numeroRegistrazionePrimenote);

                    sxVPOS.saveOrUpdate(primanotaRimborso);
                    result.put("transMsg", "CREDIT OK");
                    result.put("transMsgColor", "Green");
                    System.out.println("Credit OK!");
                    result.put("creditAmt", creditedAmt + creditAmount);

                    double newCreditAmount = creditLimit - creditAmount;
                    result.put("creditAmt1", newCreditAmount);
                    result.put("creditLmt1", newCreditAmount);

                    if (creditedAmt.equals(captdAmount)) {
                        result.put("isCreditAmt", "N");
                        result.put("creditBtn", "N");
                        selectedNetsTransazione.setState(MROldNetsTransazione.STATE_FULL_CREDITED);
                    }
                } else {
                    System.out.println("RESPONSE Code: " + response.getResponseCode().getValue());
                    System.out.println("RESPONSE Source: " + response.getResponseSource().getValue());
                    System.out.println("RESPONSE Text: " + response.getResponseText().getValue());
                    result.put("transMsg", "CREDIT KO: " + response.getResponseText().getValue());
                    result.put("transMsgColor", "Red");
                    System.out.println("Credit NOT OK!");

                    operationCredit.setError("RESPONSE Code: " + response.getResponseCode().getValue() + " - "
                            + "RESPONSE Source: " + response.getResponseSource().getValue() + " - "
                            + "RESPONSE Text: " + response.getResponseText().getValue());
                }

                sxVPOS.save(operationCredit);
                selectedNetsTransazione.setLastUpdateDate(cal.getTime());
                sxVPOS.update(selectedNetsTransazione);
                sxVPOS.flush();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }
}
