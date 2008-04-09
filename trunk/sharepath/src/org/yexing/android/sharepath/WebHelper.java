package org.yexing.android.sharepath;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;

public class WebHelper extends DefaultHandler {
	private static final String LOG_TAG = "SharePath";

	String action;
	
	@SuppressWarnings("unchecked")
	List list;
	
	public boolean Request(String uri, List results) {
		Log.v(LOG_TAG, "WebHelper.Request uri:" + uri);
		
		this.list = results;
		
		try {
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser sp = spf.newSAXParser();
			XMLReader xr = sp.getXMLReader();

			xr.setContentHandler(this);
			xr.setErrorHandler(this);

			URL url = new URL(uri);

			HttpURLConnection c = (HttpURLConnection) url.openConnection();
			c.setRequestProperty("User-Agent", "Android");
			c.setConnectTimeout(20000);
			c.connect();
			if (c.getResponseCode() == 200) {
				xr.parse(new InputSource(c.getInputStream()));
			} else {
				return false;
			}

		} catch (Exception e) {
			Log.v(LOG_TAG, "WebHelper.Request error:" + e.toString());
			return false;
		}
		Log.v(LOG_TAG, "WebHelper.Request OK");
		return true;
	}
	
	public boolean Request(String uri) {
		return Request(uri, null);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void startElement(String uri, String localName, String name,
			Attributes attr) throws SAXException {
		// TODO Auto-generated method stub
		Log.v(LOG_TAG, "startElement[uri:" + uri + " localName:" + localName
				+ " name:" + name + "]");
		if("results".equals(name)) {
			action = attr.getValue("action");
			Log.v(LOG_TAG, "action:" + action);
			if("search".equals(action)) {
				list.clear();
			}
		} else if("row".equals(name)) {
			Map m = new HashMap();
			m.put("from", attr.getValue("from"));
			m.put("start", attr.getValue("start"));
			m.put("end", attr.getValue("end"));
			m.put("level", attr.getValue("level"));
			m.put("center", attr.getValue("center"));
			m.put("path", attr.getValue("path"));
			list.add(m);
		}
	}

//	@Override
//	public void characters(char[] ch, int start, int length)
//			throws SAXException {
//		// TODO Auto-generated method stub
//		Log.v(LOG_TAG, "characters[ch:" + ch + " start:" + start + " length:"
//				+ length + "]");
//		super.characters(ch, start, length);
//	}
//
//	@Override
//	public void endElement(String uri, String localName, String name)
//			throws SAXException {
//		// TODO Auto-generated method stub
//		Log.v(LOG_TAG, "endElement[uri:" + uri + " localName:" + localName
//				+ " name:" + name + "]");
//		super.endElement(uri, localName, name);
//	}

}
