package edu.neu.madcourse.dhvanisheth.scraggle;

import java.util.Random;

public class JumbleWords {
	private static Random random = new Random();

	public String[] JumbledWord (String plainword){
		int num = random.nextInt(9);

		switch(num) {
			case 0:
				return getJumbledFormat1(plainword);

            case 1:
                return getJumbledFormat2(plainword);

            case 2:
                return getJumbledFormat3(plainword);

            case 3:
                return getJumbledFormat4(plainword);

            case 4:
                return getJumbledFormat5(plainword);

            case 5:
                return getJumbledFormat6(plainword);

            case 6:
                return getJumbledFormat7(plainword);

            case 7:
                return getJumbledFormat8(plainword);

            case 8:
                return getJumbledFormat9(plainword);

		}

		return null;
	}

	public String[] getJumbledFormat1(String plainWord) {
		String jumbled[] = new String[9];

		jumbled[0]= "" + plainWord.charAt(2);
		jumbled[1]= "" + plainWord.charAt(1);
		jumbled[2]= "" + plainWord.charAt(0);
		jumbled[3]= "" + plainWord.charAt(3);
		jumbled[4]= "" + plainWord.charAt(4);
		jumbled[5]= "" + plainWord.charAt(5);
		jumbled[6]= "" + plainWord.charAt(8);
		jumbled[7]= "" + plainWord.charAt(7);
		jumbled[8]= "" + plainWord.charAt(6);

		return jumbled;
	}

	public String[] getJumbledFormat2(String plainWord) {
		String jumbled[] = new String[9];

		jumbled[0]= "" + plainWord.charAt(1);
		jumbled[1]= "" + plainWord.charAt(0);
		jumbled[2]= "" + plainWord.charAt(6);
		jumbled[3]= "" + plainWord.charAt(2);
		jumbled[4]= "" + plainWord.charAt(5);
		jumbled[5]= "" + plainWord.charAt(7);
		jumbled[6]= "" + plainWord.charAt(3);
		jumbled[7]= "" + plainWord.charAt(4);
		jumbled[8]= "" + plainWord.charAt(8);

		return jumbled;
	}

    public String[] getJumbledFormat3(String plainWord) {
        String jumbled[] = new String[9];

        jumbled[0]= "" + plainWord.charAt(3);
        jumbled[1]= "" + plainWord.charAt(2);
        jumbled[2]= "" + plainWord.charAt(0);
        jumbled[3]= "" + plainWord.charAt(4);
        jumbled[4]= "" + plainWord.charAt(1);
        jumbled[5]= "" + plainWord.charAt(8);
        jumbled[6]= "" + plainWord.charAt(5);
        jumbled[7]= "" + plainWord.charAt(6);
        jumbled[8]= "" + plainWord.charAt(7);

        return jumbled;
    }

    public String[] getJumbledFormat4(String plainWord) {
        String jumbled[] = new String[9];

        jumbled[0]= "" + plainWord.charAt(7);
        jumbled[1]= "" + plainWord.charAt(8);
        jumbled[2]= "" + plainWord.charAt(1);
        jumbled[3]= "" + plainWord.charAt(6);
        jumbled[4]= "" + plainWord.charAt(2);
        jumbled[5]= "" + plainWord.charAt(0);
        jumbled[6]= "" + plainWord.charAt(5);
        jumbled[7]= "" + plainWord.charAt(4);
        jumbled[8]= "" + plainWord.charAt(3);

        return jumbled;
    }

    public String[] getJumbledFormat5(String plainWord) {
        String jumbled[] = new String[9];

        jumbled[0]= "" + plainWord.charAt(8);
        jumbled[1]= "" + plainWord.charAt(2);
        jumbled[2]= "" + plainWord.charAt(3);
        jumbled[3]= "" + plainWord.charAt(7);
        jumbled[4]= "" + plainWord.charAt(4);
        jumbled[5]= "" + plainWord.charAt(1);
        jumbled[6]= "" + plainWord.charAt(6);
        jumbled[7]= "" + plainWord.charAt(5);
        jumbled[8]= "" + plainWord.charAt(0);

        return jumbled;
    }

    public String[] getJumbledFormat6(String plainWord) {
        String jumbled[] = new String[9];

        jumbled[0]= "" + plainWord.charAt(4);
        jumbled[1]= "" + plainWord.charAt(5);
        jumbled[2]= "" + plainWord.charAt(6);
        jumbled[3]= "" + plainWord.charAt(3);
        jumbled[4]= "" + plainWord.charAt(1);
        jumbled[5]= "" + plainWord.charAt(7);
        jumbled[6]= "" + plainWord.charAt(2);
        jumbled[7]= "" + plainWord.charAt(0);
        jumbled[8]= "" + plainWord.charAt(8);

        return jumbled;
    }

    public String[] getJumbledFormat7(String plainWord) {
        String jumbled[] = new String[9];

        jumbled[0]= "" + plainWord.charAt(3);
        jumbled[1]= "" + plainWord.charAt(2);
        jumbled[2]= "" + plainWord.charAt(5);
        jumbled[3]= "" + plainWord.charAt(1);
        jumbled[4]= "" + plainWord.charAt(4);
        jumbled[5]= "" + plainWord.charAt(6);
        jumbled[6]= "" + plainWord.charAt(0);
        jumbled[7]= "" + plainWord.charAt(8);
        jumbled[8]= "" + plainWord.charAt(7);

        return jumbled;
    }

    public String[] getJumbledFormat8(String plainWord) {
        String jumbled[] = new String[9];

        jumbled[0]= "" + plainWord.charAt(2);
        jumbled[1]= "" + plainWord.charAt(1);
        jumbled[2]= "" + plainWord.charAt(8);
        jumbled[3]= "" + plainWord.charAt(0);
        jumbled[4]= "" + plainWord.charAt(3);
        jumbled[5]= "" + plainWord.charAt(7);
        jumbled[6]= "" + plainWord.charAt(4);
        jumbled[7]= "" + plainWord.charAt(5);
        jumbled[8]= "" + plainWord.charAt(6);

        return jumbled;
    }

    public String[] getJumbledFormat9(String plainWord) {
        String jumbled[] = new String[9];

        jumbled[0]= "" + plainWord.charAt(0);
        jumbled[1]= "" + plainWord.charAt(6);
        jumbled[2]= "" + plainWord.charAt(7);
        jumbled[3]= "" + plainWord.charAt(5);
        jumbled[4]= "" + plainWord.charAt(1);
        jumbled[5]= "" + plainWord.charAt(8);
        jumbled[6]= "" + plainWord.charAt(4);
        jumbled[7]= "" + plainWord.charAt(3);
        jumbled[8]= "" + plainWord.charAt(2);

        return jumbled;
    }
	
}