import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.TreeSet;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class LottoClass {
	/* 지난주 당첨번호 (보너스 번호 포함)를 파싱해서 리턴하는 함수 */
	public List<Integer> parseLastWeekNumbers() throws IOException {
		Document doc = Jsoup.connect("https://www.dhlottery.co.kr/gameResult.do?method=byWin").get();
		Elements numbers=doc.select(".ball_645");
		List<Integer> lastWeekNumbers=new ArrayList<>();
		String[] str=numbers.text().split(" ");
		for(int i=0;i<str.length;i++) {
			lastWeekNumbers.add(Integer.parseInt(str[i]));
		}
		return lastWeekNumbers;
	}
	
	/* 저번주 당첨번호를 최대 2개 포함한 랜덤 번호 6개를 리턴하는 함수 */
	/* 이 부분은 저만의 규칙입니다. 지난 주 번호 한두개가 다음 주에 포함될 확률이 조금 높다고 본 적이 있습니다. */
	public TreeSet<Integer> getRandomNumbers(List<Integer> lastWeekNumbers){
		Random random=new Random();
		random.setSeed(System.currentTimeMillis());
		int includeCount=random.nextInt(3);//저번주 번호 포함 갯수 최대 2개 포함
		TreeSet<Integer> randomNumbers=new TreeSet<>();
		for(int i=0;i<includeCount;i++) {
			randomNumbers.add(lastWeekNumbers.get(random.nextInt(lastWeekNumbers.size())));
		}
		while(randomNumbers.size()<6) {
			randomNumbers.add(random.nextInt(45)+1);
		}
		return randomNumbers;
	}
	
	public static void main(String[] args) throws IOException {
		LottoClass lotto=new LottoClass();
		List<Integer> lastWeekNumbers=lotto.parseLastWeekNumbers();// 지난 주 당첨번호
		TreeSet<Integer> randomNumbers; // 랜덤 번호 6개씩 저장될 공간
		int lineCount=5; // 로또번호를 뽑을 라인 수 1라인 당 6개의 숫자 

		for(int i=0;i<lineCount;i++) {
			randomNumbers=lotto.getRandomNumbers(lastWeekNumbers);
			System.out.println(randomNumbers.toString()+"\n");
			/* 시드값을 현재시간의 밀리초로 주었으나 그 마저도 반복문을 돌면 중복되는 케이스가 있어서 강제적으로 시드가 중복되지 않도록 제어 */
			try {
				Thread.sleep(1);
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
