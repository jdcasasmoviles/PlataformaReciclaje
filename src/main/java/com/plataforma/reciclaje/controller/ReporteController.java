package com.plataforma.reciclaje.controller;
import com.plataforma.reciclaje.model.RecyclingRecord;
import com.plataforma.reciclaje.model.User;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPCell;
import java.util.List;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class ReporteController {
    
     public ReporteController(){}
     
    public void generarExcel(RegistroReciclajeController registroReciclajeController,
            UsuarioController usuarioController,User currentUser,String ruta){
        org.apache.poi.ss.usermodel.Workbook workbook = new org.apache.poi.xssf.usermodel.XSSFWorkbook();
    org.apache.poi.ss.usermodel.Sheet sheet = workbook.createSheet("Reporte Reciclaje");

    // Crear estilos
    org.apache.poi.ss.usermodel.CellStyle headerStyle = workbook.createCellStyle();
    org.apache.poi.ss.usermodel.Font headerFont = workbook.createFont();
    headerFont.setBold(true);
    headerFont.setColor(org.apache.poi.ss.usermodel.IndexedColors.WHITE.getIndex());
    headerStyle.setFont(headerFont);
    headerStyle.setFillForegroundColor(org.apache.poi.ss.usermodel.IndexedColors.DARK_BLUE.getIndex());
    headerStyle.setFillPattern(org.apache.poi.ss.usermodel.FillPatternType.SOLID_FOREGROUND);
    headerStyle.setAlignment(org.apache.poi.ss.usermodel.HorizontalAlignment.CENTER);

    org.apache.poi.ss.usermodel.CellStyle oddRowStyle = workbook.createCellStyle();
    oddRowStyle.setFillForegroundColor(org.apache.poi.ss.usermodel.IndexedColors.GREY_25_PERCENT.getIndex());
    oddRowStyle.setFillPattern(org.apache.poi.ss.usermodel.FillPatternType.SOLID_FOREGROUND);

    org.apache.poi.ss.usermodel.CellStyle evenRowStyle = workbook.createCellStyle();
    evenRowStyle.setFillForegroundColor(org.apache.poi.ss.usermodel.IndexedColors.LEMON_CHIFFON.getIndex());
    evenRowStyle.setFillPattern(org.apache.poi.ss.usermodel.FillPatternType.SOLID_FOREGROUND);

    int rowNum = 0;

    // === Total reciclado por usuario ===
    org.apache.poi.ss.usermodel.Row header1 = sheet.createRow(rowNum++);
    header1.createCell(0).setCellValue("MATERIAL");
    header1.createCell(1).setCellValue("CANTIDAD");
    header1.createCell(2).setCellValue("PUNTOS");
    for(int c=0;c<3;c++) header1.getCell(c).setCellStyle(headerStyle);

    int idx = 0;
    for (RecyclingRecord r : registroReciclajeController.findTotalReciclado(currentUser)) {
        org.apache.poi.ss.usermodel.Row row = sheet.createRow(rowNum++);
        row.createCell(0).setCellValue(r.getMaterial().name());
        row.createCell(1).setCellValue(r.getQuantity());
        row.createCell(2).setCellValue(r.getPoints());
        org.apache.poi.ss.usermodel.CellStyle style = (idx%2==0)?evenRowStyle:oddRowStyle;
        for(int c=0;c<3;c++) row.getCell(c).setCellStyle(style);
        idx++;
    }

    rowNum++; // fila vacía

    // === Top Usuarios ===
    org.apache.poi.ss.usermodel.Row header2 = sheet.createRow(rowNum++);
    header2.createCell(0).setCellValue("USUARIO");
    header2.createCell(1).setCellValue("PUNTOS");
    for(int c=0;c<2;c++) header2.getCell(c).setCellStyle(headerStyle);

    idx=0;
    for (User u : usuarioController.findTopUsers()) {
        org.apache.poi.ss.usermodel.Row row = sheet.createRow(rowNum++);
        row.createCell(0).setCellValue(u.getName());
        row.createCell(1).setCellValue(u.getPoints());
        org.apache.poi.ss.usermodel.CellStyle style = (idx%2==0)?evenRowStyle:oddRowStyle;
        for(int c=0;c<2;c++) row.getCell(c).setCellStyle(style);
        idx++;
    }

    rowNum++; // fila vacía

    // === Materiales más reciclados ===
    org.apache.poi.ss.usermodel.Row header3 = sheet.createRow(rowNum++);
    header3.createCell(0).setCellValue("MATERIAL");
    header3.createCell(1).setCellValue("CANTIDAD");
    for(int c=0;c<2;c++) header3.getCell(c).setCellStyle(headerStyle);

    idx=0;
    for (RecyclingRecord r : registroReciclajeController.findAllTotalReciclado()) {
        org.apache.poi.ss.usermodel.Row row = sheet.createRow(rowNum++);
        row.createCell(0).setCellValue(r.getMaterial().name());
        row.createCell(1).setCellValue(r.getQuantity());
        org.apache.poi.ss.usermodel.CellStyle style = (idx%2==0)?evenRowStyle:oddRowStyle;
        for(int c=0;c<2;c++) row.getCell(c).setCellStyle(style);
        idx++;
    }

    // Autoajustar columnas
    for(int i=0;i<3;i++) sheet.autoSizeColumn(i);

    // Guardar archivo
    try(java.io.FileOutputStream fos = new java.io.FileOutputStream(ruta + "\\reporteExcel.xlsx")){
        workbook.write(fos);
        workbook.close();
    }   catch (FileNotFoundException ex) {
            Logger.getLogger(ReporteController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ReporteController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void generarPdf(
            RegistroReciclajeController registroReciclajeController,
            UsuarioController usuarioController,
            User currentUser,
            String ruta,StringBuilder sb) {
        try {
               Document document = new Document();
             PdfWriter.getInstance(document, new FileOutputStream(ruta + "\\reportePDF.pdf"));
               document.open();

        // Título principal
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, BaseColor.BLUE);
        Paragraph title = new Paragraph("REPORTE DE RECICLAJE", titleFont);
        title.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(title);
        document.add(new Paragraph("\n")); // espacio

        // === Total reciclado por usuario ===
        Font sectionFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.BLACK);
        document.add(new Paragraph("TOTAL RECICLADO POR EL USUARIO", sectionFont));
        document.add(new Paragraph("\n"));

        PdfPTable table1 = new PdfPTable(3);
        table1.setWidthPercentage(100);
        table1.setWidths(new int[]{3,2,2});

        // Encabezados
        String[] headers1 = {"MATERIAL", "CANTIDAD", "PUNTOS"};
        for(String h : headers1){
            PdfPCell cell = new PdfPCell(new Paragraph(h, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.WHITE)));
            cell.setBackgroundColor(BaseColor.DARK_GRAY);
            cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            table1.addCell(cell);
        }

        // Filas
        int idx = 0;
        List<RecyclingRecord> listaUsuario = registroReciclajeController.findTotalReciclado(currentUser);
        for(RecyclingRecord r : listaUsuario){
            BaseColor bg = (idx % 2 == 0) ? BaseColor.LIGHT_GRAY : BaseColor.WHITE;
            table1.addCell(createStyledCell(r.getMaterial().name(), bg));
            table1.addCell(createStyledCell(String.valueOf(r.getQuantity()), bg));
            table1.addCell(createStyledCell(String.valueOf(r.getPoints()), bg));
            idx++;
        }
        document.add(table1);
        document.add(new Paragraph("\n"));

        // === Top usuarios ===
        document.add(new Paragraph("TOP USUARIOS", sectionFont));
        document.add(new Paragraph("\n"));

        PdfPTable table2 = new PdfPTable(2);
        table2.setWidthPercentage(100);
        table2.setWidths(new int[]{3,2});

        String[] headers2 = {"USUARIO", "PUNTOS"};
        for(String h : headers2){
            PdfPCell cell = new PdfPCell(new Paragraph(h, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.WHITE)));
            cell.setBackgroundColor(BaseColor.DARK_GRAY);
            cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            table2.addCell(cell);
        }

        idx=0;
        List<User> topUsers = usuarioController.findTopUsers();
        for(User u : topUsers){
            BaseColor bg = (idx % 2 == 0) ? BaseColor.LIGHT_GRAY : BaseColor.WHITE;
            table2.addCell(createStyledCell(u.getName(), bg));
            table2.addCell(createStyledCell(String.valueOf(u.getPoints()), bg));
            idx++;
        }
        document.add(table2);
        document.add(new Paragraph("\n"));

        // === Materiales más reciclados ===
        document.add(new Paragraph("MATERIALES MÁS RECICLADOS EN GENERAL", sectionFont));
        document.add(new Paragraph("\n"));

        PdfPTable table3 = new PdfPTable(2);
        table3.setWidthPercentage(100);
        table3.setWidths(new int[]{3,2});

        String[] headers3 = {"MATERIAL", "CANTIDAD"};
        for(String h : headers3){
            PdfPCell cell = new PdfPCell(new Paragraph(h, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.WHITE)));
            cell.setBackgroundColor(BaseColor.DARK_GRAY);
            cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            table3.addCell(cell);
        }

        idx=0;
        List<RecyclingRecord> listaGeneral = registroReciclajeController.findAllTotalReciclado();
        for(RecyclingRecord r : listaGeneral){
            BaseColor bg = (idx % 2 == 0) ? BaseColor.LIGHT_GRAY : BaseColor.WHITE;
            table3.addCell(createStyledCell(r.getMaterial().name(), bg));
            table3.addCell(createStyledCell(String.valueOf(r.getQuantity()), bg));
            idx++;
        }
        document.add(table3);

        document.close();
         } catch (FileNotFoundException ex) {
             Logger.getLogger(ReporteController.class.getName()).log(Level.SEVERE, null, ex);
         } catch (DocumentException ex) {
             Logger.getLogger(ReporteController.class.getName()).log(Level.SEVERE, null, ex);
         }
         
    }

    private PdfPCell createStyledCell(String text, BaseColor bgColor){
    PdfPCell cell = new PdfPCell(new Paragraph(text, FontFactory.getFont(FontFactory.HELVETICA, 12)));
    cell.setBackgroundColor(bgColor);
    cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
    return cell;
}
    
    public void generarTxt(StringBuilder sb, String ruta) {
         try {
             java.nio.file.Files.write(
                     java.nio.file.Paths.get(ruta + "\\reporteTexto.txt"),
                     sb.toString().getBytes()
             );
         } catch (IOException ex) {
             Logger.getLogger(ReporteController.class.getName()).log(Level.SEVERE, null, ex);
         }
    }

    
}
