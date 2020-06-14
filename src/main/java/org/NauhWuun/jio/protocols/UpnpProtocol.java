package org.NauhWuun.jio.protocols;

import org.bitlet.weupnp.GatewayDevice;
import org.bitlet.weupnp.GatewayDiscover;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Map;

public class UpnpProtocol
{
	protected void setupUpnp(int port) {
		try {
			GatewayDiscover discover = new GatewayDiscover();
			Map<InetAddress, GatewayDevice> devices = discover.discover();
			for (Map.Entry<InetAddress, GatewayDevice> entry : devices.entrySet()) {
				GatewayDevice gw = entry.getValue();

				gw.deletePortMapping(port, "TCP");
				gw.addPortMapping(port & 0xffff, port & 0xffff,
						gw.getLocalAddress().getHostAddress(), "TCP", "Upnp Port Mapping");
			}
		} catch (IOException | SAXException | ParserConfigurationException e) {
			e.fillInStackTrace();
		}
	}
}
