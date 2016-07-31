import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

public class CafeteriaManager {
	CafeteriaData cfd = new CafeteriaData();

	CafeteriaManager() {
	}

	public CafeteriaData getData(String date, String storeval) throws Exception {
		int count=0;
		while (count != 3) {//������ 3��ġ�� �����͸� �ҷ����ڴ� �̶��� calvalue �����κ�
			URL url = new URL("http://cautis.cau.ac.kr/SMT/tis/sMwsFoo010/selectList.do");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Accept-Language", "ko-kr,ko;q=0.8,en-us;q=0.5,en;q=0.3");

			// ������
			String param = URLEncoder.encode("today", "UTF-8") + "=" + URLEncoder.encode(date, "UTF-8");
			param += "&" + URLEncoder.encode("calvalue", "UTF-8") + "=" + URLEncoder.encode(Integer.toString(count), "UTF-8");//count�� calvalue�� tostring�� �Ѱ���.
			param += "&" + URLEncoder.encode("store", "UTF-8") + "=" + URLEncoder.encode(storeval, "UTF-8");
			/*
			 * store value 11 ------- University Club -102 11F 07 ------- 203 B1
			 * 08 ------- 308 12 ------- 309 02 ------- 205 1F 03 ------- 205 B1
			 * 06 ------- 303 B1
			 */

			// ����
			OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
			osw.write(param);
			osw.flush();
			Parser p = new Parser();
			Document doc = p.parseXML(conn.getInputStream());
			NodeList descNodes = doc.getElementsByTagName("raw");// xml�� raw��
																	// �ɰ��� �κ�

			for (int i = 0; i < descNodes.getLength(); i++) {

				for (Node node = descNodes.item(i).getFirstChild(); node != null; node = node.getNextSibling()) { // ù��°
																													// �ڽ���
																													// ��������
																													// ����������
																													// ����
																													// ������
																													// ����

					if (node.getNodeName().equals("menunm")) {// �
																// �޴�����(����?���?�̷���)
						// System.out.println(node.getTextContent());
						cfd.mealsection.add(node.getTextContent());
					} else if (node.getNodeName().equals("tm")) {// �ð�
						// System.out.println(node.getTextContent());
						cfd.Time.add(node.getTextContent());
					} else if (node.getNodeName().equals("amt")) {// ����
						// System.out.println(node.getTextContent());
						cfd.mealprice.add(node.getTextContent());
					} else if (node.getNodeName().equals("menunm1")) {// ����
																		// �Ĵ�+Į�θ�
						String str = node.getTextContent();
						// System.out.println(str);
						String[] menulist = str.split("<br>");
						if (str.length() == 9) {
							cfd.menu[i].add(str);
						} else {

							for (int j = 0; j < menulist.length; j++)
								cfd.menu[i].add(menulist[j]);
						}

						// System.out.println("Test");

					}
				}
			}
			count++;
		}
		return cfd;
	}
}
