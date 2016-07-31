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
		while (count != 3) {//앞으로 3일치의 데이터를 불러오겠다 이뜻임 calvalue 조절부분
			URL url = new URL("http://cautis.cau.ac.kr/SMT/tis/sMwsFoo010/selectList.do");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Accept-Language", "ko-kr,ko;q=0.8,en-us;q=0.5,en;q=0.3");

			// 데이터
			String param = URLEncoder.encode("today", "UTF-8") + "=" + URLEncoder.encode(date, "UTF-8");
			param += "&" + URLEncoder.encode("calvalue", "UTF-8") + "=" + URLEncoder.encode(Integer.toString(count), "UTF-8");//count를 calvalue로 tostring로 넘겨줌.
			param += "&" + URLEncoder.encode("store", "UTF-8") + "=" + URLEncoder.encode(storeval, "UTF-8");
			/*
			 * store value 11 ------- University Club -102 11F 07 ------- 203 B1
			 * 08 ------- 308 12 ------- 309 02 ------- 205 1F 03 ------- 205 B1
			 * 06 ------- 303 B1
			 */

			// 전송
			OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
			osw.write(param);
			osw.flush();
			Parser p = new Parser();
			Document doc = p.parseXML(conn.getInputStream());
			NodeList descNodes = doc.getElementsByTagName("raw");// xml이 raw로
																	// 쪼개는 부분

			for (int i = 0; i < descNodes.getLength(); i++) {

				for (Node node = descNodes.item(i).getFirstChild(); node != null; node = node.getNextSibling()) { // 첫번째
																													// 자식을
																													// 시작으로
																													// 마지막까지
																													// 다음
																													// 형제를
																													// 실행

					if (node.getNodeName().equals("menunm")) {// 어떤
																// 메뉴인지(조식?양식?이런거)
						// System.out.println(node.getTextContent());
						cfd.mealsection.add(node.getTextContent());
					} else if (node.getNodeName().equals("tm")) {// 시간
						// System.out.println(node.getTextContent());
						cfd.Time.add(node.getTextContent());
					} else if (node.getNodeName().equals("amt")) {// 가격
						// System.out.println(node.getTextContent());
						cfd.mealprice.add(node.getTextContent());
					} else if (node.getNodeName().equals("menunm1")) {// 음식
																		// 식단+칼로리
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
