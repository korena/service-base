/* 
 * The MIT License
 *
==================================================================================
 * Copyright 2016 SIPHYC SYSTEMS Sdn Bhd All Rights Reserved.
 *
 * project reference code contributed by Moaz Korena <korena@siphyc.com,moazkorena@gmail.com>
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.siphyc.utils;

import java.util.Calendar;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Properties;
import java.util.Random;
import java.util.Scanner;
import java.util.TimeZone;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.slf4j.LoggerFactory;

public class Utilities {

    private static final SecureRandom random = new SecureRandom();

    public static final org.slf4j.Logger logger = LoggerFactory.getLogger(Utilities.class);

    private static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private static final Random rnd = new Random();

    public static final Properties conf = loadConfig();

    public static String generateNonce() {
        return new BigInteger(130, random).toString(32);
    }

    public static boolean isInteger(String s) {
        return isInteger(s, 10);
    }

    public static boolean isInteger(String s, int radix) {
        Scanner sc = new Scanner(s.trim());
        if (!sc.hasNextInt(radix)) {
            return false;
        }
        // we know it starts with a valid int, now make sure
        // there's nothing left!
        sc.nextInt(radix);
        return !sc.hasNext();
    }

    public static boolean isIntegerCheap(String s, int radix) {
        if (s.isEmpty()) {
            return false;
        }
        for (int i = 0; i < s.length(); i++) {
            if (i == 0 && s.charAt(i) == '-') {
                if (s.length() == 1) {
                    return false;
                } else {
                    continue;
                }
            }
            if (Character.digit(s.charAt(i), radix) < 0) {
                return false;
            }
        }
        return true;
    }

    public static String hashWithAlgChoice(String hashable, String alg) {
        if (alg.equalsIgnoreCase("SHA-256")) {
            MessageDigest md;
            try {
                md = MessageDigest.getInstance("SHA-256");
                byte[] digest = md.digest(hashable.getBytes()); // Missing charset
//                String hex = Hex.encodeHexString(digest);
                return Arrays.toString(Base64.encodeBase64(digest));
            } catch (NoSuchAlgorithmException ex) {
                logger.debug("Could not perform hashing:\n" + ex);
                return null;
            }
        } else if (alg.equalsIgnoreCase("SHA1")) {
            return org.apache.commons.codec.digest.DigestUtils.sha1Hex(hashable);
        } else {
            return null;
        }
    }

    public static byte[] hash256WithByteReturn(String hashable) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            return md.digest(hashable.getBytes());
        } catch (NoSuchAlgorithmException ex) {
            logger.debug("Could not perform hashing:\n" + ex);
            return null;
        }
    }

    public static byte[] getHalfOfByteArray(byte[] array) {
        return Arrays.copyOf(array, (array.length) / 2);
    }

    public static String base64ByteEncoding(byte[] array) {
        return Base64.encodeBase64String(array);
    }

    public static String getHMACSHA256(String secret, String message) {
        try {
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
            sha256_HMAC.init(secret_key);

            String hash = Base64.encodeBase64String(sha256_HMAC.doFinal(message.getBytes()));
            return hash;
        } catch (Exception e) {
            System.out.println("Error");
        }
        return null;
    }

    public static byte[] getFileBytes(String url) {

        InputStream stream = null;
        try {
            URL resource = getResource(url);
            stream = resource.openStream();
            return IOUtils.toByteArray(stream);

        } catch (IOException ex) {
            logger.error("<>shit hit the fan !" + ex);
        } finally {
            try {
                stream.close();
            } catch (NullPointerException | IOException ex) {

                logger.error("<>the stream might have been null ..." + ex);
            }
        }
        return null;
    }

    public static URL getResource(String resource) {
        URL url;

        //Try with the Thread Context Loader. 
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        if (classLoader != null) {
            url = classLoader.getResource(resource);
            if (url != null) {
                return url;
            }
        }
        //Last ditch attempt. Get the resource from the classpath.
        return ClassLoader.getSystemResource(resource);
    }

    public static Properties loadConfig() {
        try {
            Properties prop = new Properties();
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            prop.load(classLoader.getResourceAsStream("active.properties"));
            return prop;
        } catch (IOException ex) {
            logger.error("<>shit hit the fan !" + ex);
            return null;
        }
    }

    public static String randomString(int len) {
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        }
        return sb.toString();
    }


    public static String GetUTCdatetimeAsString() {
        String DATEFORMAT = "yyyy-MM-dd HH:mm:ss";
        final SimpleDateFormat sdf = new SimpleDateFormat(DATEFORMAT);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        final String utcTime = sdf.format(new Date());
        return utcTime;
    }

    public static Date combineDateTime(String date, String time) throws ParseException {

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String dateString = date + " " + time;
        return dateFormat.parse(dateString);
    }

    public static Date combineDateTime(Date date, Date time) {
        Calendar dateCalendar = GregorianCalendar.getInstance();
        dateCalendar.setTimeZone(TimeZone.getTimeZone("UTC"));
        dateCalendar.setTime(date);
        Calendar timeCalendar = GregorianCalendar.getInstance();
        timeCalendar.setTime(time);

        Calendar combinedCalendar = GregorianCalendar.getInstance();
        // date
        combinedCalendar.set(Calendar.YEAR, dateCalendar.get(Calendar.YEAR));
        combinedCalendar.set(Calendar.MONTH, dateCalendar.get(Calendar.MONTH));
        combinedCalendar.set(Calendar.DAY_OF_MONTH, dateCalendar.get(Calendar.DAY_OF_MONTH));
        //time
        combinedCalendar.set(Calendar.HOUR_OF_DAY, timeCalendar.get(Calendar.HOUR_OF_DAY));
        combinedCalendar.set(Calendar.MINUTE, timeCalendar.get(Calendar.MINUTE));

        return combinedCalendar.getTime();
    }

    public static Date getDatePart(Date dateObject) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date dateWithoutTime = sdf.parse(sdf.format(dateObject));
        return dateWithoutTime;
    }

    public static Date getDatePart(String dateString) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.parse(dateString);
    }

    public static String getYear(Date theDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(theDate);
        int year = cal.get(Calendar.YEAR);
        return String.valueOf(year);
    }

    public static String getMonth(Date theDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(theDate);
        int monthNum = cal.get(Calendar.MONTH);
        return getMonthForInt(monthNum);
    }

    public static String getMonthForInt(int num) {
        String month = "wrong";
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months = dfs.getMonths();
        if (num >= 0 && num <= 11) {
            month = months[num];
        }
        return month;
    }

    public static Date getTimePart(String timeString) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        return sdf.parse(timeString);
    }

    public static Date getTimePart(Date dateObject) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        Date timeWithoutDate = sdf.parse(sdf.format(dateObject));
        return timeWithoutDate;
    }

    public static void main(String[] args) throws ParseException {

        String date = "2015-12-08";
        String time = "11:54";

        Date combinedFirst = combineDateTime(date, time);
        Date datePartExtract = getDatePart(combineDateTime(date, time));
        Date datePartExtractO = getDatePart(datePartExtract);
        Date timePartExtract = getTimePart(combineDateTime(date, time));
        Date timePartExtractO = getTimePart(timePartExtract);
        Date recombined = combineDateTime(datePartExtract, timePartExtract);

        System.out.println("Combined date: " + combinedFirst);
        System.out.println("Date part: " + datePartExtract);
        System.out.println("Date part O: " + datePartExtractO);
        System.out.println("Time part: " + timePartExtract);
        System.out.println("Time part O: " + timePartExtractO);
        System.out.println("Recombined date: " + recombined);
    }
}
