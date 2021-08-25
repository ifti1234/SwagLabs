package com.swaglabs.utilities;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Comparator;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.Borders;
import org.apache.poi.xwpf.usermodel.BreakType;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.UnderlinePatterns;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import cucumber.api.Scenario;
import io.qameta.allure.Allure;

public class Reporter {

    public boolean embedScreenshotsIntoWord(String wordFilePath,String scenarioName) {

        boolean status = false;
        InputStream pic = null;
        File file = null;
        File imageFolder = null;
        FileOutputStream out = null;
        XWPFDocument docx = null;
        try {
            docx = new XWPFDocument();
            XWPFParagraph para = docx.createParagraph();
            XWPFRun run = para.createRun();
            run.setText(scenarioName);
            para.setBorderLeft(Borders.BASIC_THIN_LINES);
            para.setBorderRight(Borders.BASIC_THIN_LINES);
            run.setBold(true);
            run.setFontSize(15);
            run.setUnderline(UnderlinePatterns.SINGLE);
            run.addBreak();
            para.setAlignment(ParagraphAlignment.CENTER);
            out = new FileOutputStream(wordFilePath + File.separator + scenarioName + ".docx");
            imageFolder = new File(wordFilePath);
            System.out.println("Last Modified Time :");
            File[] filArr = imageFolder.listFiles();
            filArr = Arrays.stream(filArr).sorted(Comparator.comparingLong(f -> f.lastModified())).toArray(File[]::new);
            for (int i = 0; i < filArr.length; i++) {
                String screenshot_name = System.currentTimeMillis() + ".png";
                if (filArr[i].getName().toString().endsWith(".png")) {
                    Image image = ImageIO.read(filArr[i]);
                    BufferedImage buffered = (BufferedImage) image;
                    file = new File(wordFilePath + File.separator + screenshot_name);
                    ImageIO.write(buffered, "png", file);
                    pic = new FileInputStream(file);
                    run.addBreak();
                    run.addPicture(pic, XWPFDocument.PICTURE_TYPE_PNG, screenshot_name, Units.toEMU(350),
                            Units.toEMU(500));
                    run.addBreak();
                    run.addBreak();
                    run.setText(scenarioName + " - " + i + ".png");
                    TimeUnit.SECONDS.sleep(1);
                }
            }
            docx.write(out);
            for (File delFile : new File(wordFilePath).listFiles()) {
                if (delFile.getName().endsWith(".png"))
                    delFile.delete();
            }
            status = true;
        } catch (Exception e) {
            Log.info(e.getMessage());
        } finally {
            try {
                pic.close();
                out.flush();
                out.close();
                docx.close();
            } catch (IOException e) {
                Log.info(e.getMessage());
            }
        }
        return status;
    }

    public static boolean wordReportCreator(Scenario sc, String wordFilePath, int sheetNo, String customerid)
            throws Exception {
        String wordFileName = null;
        if (wordFileName == null) {
            wordFileName = sc.getName().split(":")[1] + "->" + sc.getStatus();
        }
        return embedScreenshotsIntoWord(wordFilePath, wordFileName, sc, customerid);
    }

    public static boolean embedScreenshotsIntoWord(String wordFilePath, String wordFileName, Scenario sc,
                                                   String customerid) throws Exception {

        InputStream pic = null;
        File file = null;
        XWPFDocument docx = new XWPFDocument();
        XWPFParagraph para = docx.createParagraph();
        XWPFRun run = para.createRun();
        para.setBorderLeft(Borders.BASIC_THIN_LINES);
        para.setBorderRight(Borders.BASIC_THIN_LINES);
        run.setBold(true);
        run.setFontSize(15);
        run.setUnderline(UnderlinePatterns.SINGLE);
        para.setAlignment(ParagraphAlignment.CENTER);
        File imageFolder = new File(wordFilePath);
        File[] filArr = imageFolder.listFiles();
        filArr = Arrays.stream(filArr).sorted(Comparator.comparingLong(f -> f.lastModified())).toArray(File[]::new);
        for (int i = 0; i < filArr.length; i++) {
            String screenshotName = System.currentTimeMillis() + ".png";
            if (filArr[i].getName().toString().endsWith(".png")) {
                Image image = ImageIO.read(filArr[i]);
                BufferedImage buffered = (BufferedImage) image;
                file = new File(wordFilePath + "/" + screenshotName);
                ImageIO.write(buffered, "png", file);
                pic = new FileInputStream(file);
                run.setText(sc.getName() + " -> " + "\t" + i + ".png");
                run.addBreak();
                run.setText("Rim ID:" + customerid);
                run.addBreak();
                run.addPicture(pic, XWPFDocument.PICTURE_TYPE_PNG, screenshotName, Units.toEMU(350), Units.toEMU(500));
                run.addBreak(BreakType.PAGE);
                TimeUnit.SECONDS.sleep(1);
            }
        }
        run.setText(sc.getStatus() + " -> At this Step in Screenshot ");
        FileOutputStream out = new FileOutputStream(wordFilePath + "/" + wordFileName.trim() + ".docx");
        docx.write(out);
        File fi = new File(wordFilePath);
        File[] fii = fi.listFiles();
        if (fii != null) {
            for (File delFile : fii) {
                if (delFile.getName().endsWith(".png")) {
                    boolean deletedFile = delFile.delete();
                    System.out.println("Is File deleted:" + deletedFile);
                }
            }
        }
        boolean status = false;
        status = true;
        pic.close();
        out.flush();
        out.close();
        docx.close();

        return status;
    }

    public void addAllureAttachment(String name, Object file) throws IOException {
        if (file instanceof File[]) {
            for (File fi : (File[]) file) {
                switch (FilenameUtils.getExtension(fi.getName())) {
                    case "json":
                        Allure.getLifecycle().addAttachment(name, "application/json", "json",
                                FileUtils.convertFileToByteArray(fi));
                        break;
                    case "jpeg":
                        Allure.getLifecycle().addAttachment(name, "application/jpeg", "jpeg",
                                FileUtils.convertFileToByteArray(fi));
                        break;
                    case "png":
                        Allure.getLifecycle().addAttachment(name, "application/png", "png",
                                FileUtils.convertFileToByteArray(fi));
                        break;
                    case "html":
                        Allure.getLifecycle().addAttachment(name, "application/html", "html",
                                FileUtils.convertFileToByteArray(fi));
                        break;
                    default:
                }
            }
        } else if (file instanceof String) {
            Allure.getLifecycle().addAttachment(name, "application/json", "json", ((String) file).getBytes("UTF-8"));

        }
    }

}
