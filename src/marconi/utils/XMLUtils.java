/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package marconi.utils;

import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author felipe
 */
public class XMLUtils {
    
public static NodeList getDataXML(String path_file,String element)
{
    try {
        
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(path_file);
        doc.getDocumentElement().normalize();
        return  doc.getElementsByTagName(element);
    } catch (SAXException ex) {
        System.err.println("excepcion");
    } catch (IOException ex) {
        System.err.println("excepcion");
    } catch (ParserConfigurationException ex) {
         System.err.println("excepcion");
    }
    return null;
}
}
