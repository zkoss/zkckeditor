import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Random;

import org.zkoss.util.Locales;

public class TestArea {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		DateFormat df = DateFormat.getDateTimeInstance(DateFormat.DEFAULT,
				DateFormat.SHORT, Locales.getCurrent());
		System.out.println(((SimpleDateFormat) df).toPattern());
	}

}
