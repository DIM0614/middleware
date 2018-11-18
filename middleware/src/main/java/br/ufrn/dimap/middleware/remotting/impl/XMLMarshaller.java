package br.ufrn.dimap.middleware.remotting.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;

import br.ufrn.dimap.middleware.remotting.interfaces.Marshaller;

/**
 * Marshaller that uses XML as marshalling format, making use
 * of JAXB (Java Architecture for XML Binding).
 * 
 * The class of the "root" object to be marshalled must be
 * annotated with XmlRootElement. See javax.xml.bind
 * documentation for details.
 * 
 * Note that, by default, only public getter/setter pairs and
 * public fields are bound to XML in JAXB. This can be changed
 * by the XMLAccessorType annotation. 
 * 
 * Note also that JAXB requires a default constructor 
 * (public constructor with no arguments) in all marshalled
 * objects for unmarshalling.
 * 
 * @author carlosemv
 */
public class XMLMarshaller implements Marshaller {

	public <T> ByteArrayOutputStream marshal(T object) throws IOException {
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		
		try {
			JAXBContext jc = JAXBContext.newInstance(object.getClass());
			javax.xml.bind.Marshaller marshaller = jc.createMarshaller();
			
			marshaller.marshal(object, byteStream);
		} catch (JAXBException e) {
			e.printStackTrace();
		}

		return byteStream;
	}

	public <T> T unmarshal(ByteArrayInputStream byteStream, Class<T> tgtClass) throws IOException, ClassNotFoundException {
		T result = null;
		
		try {
			JAXBContext jc = JAXBContext.newInstance(tgtClass);
			Unmarshaller unmarshaller = jc.createUnmarshaller();

			result = (T) unmarshaller.unmarshal(byteStream);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		
		return result;
	}

}