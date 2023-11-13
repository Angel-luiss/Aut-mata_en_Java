package modelo;
/**
 *
 * @author angel
 */

import java.awt.Insets;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import javax.swing.JTextArea;
import javax.swing.TransferHandler;

public class ArchivoTXT {

    private final JTextArea textArea;//encapsulado a solo esta clase 
    private final JTextArea jTextArea1;
    private final JTextArea jTextArea2;
    private final JTextArea jTextArea3;
    private final JTextArea jTextArea4;
    
    //constructor encapsulado a publico, parametros y campos XD
    public ArchivoTXT(JTextArea textArea, JTextArea jTextArea1, JTextArea jTextArea2, JTextArea jTextArea3, JTextArea jTextArea4) {
        this.textArea = textArea;
        this.jTextArea1 = jTextArea1;//campos
        this.jTextArea2 = jTextArea2;
        this.jTextArea3 = jTextArea3;
        this.jTextArea4 = jTextArea4;
        initArrastrarYsoltar(); //Método initArrastrarYsoltar para el archivo 
    }
    //metodos privados porque solo los usare desde mi clas ArchivTxT XD
    private void initArrastrarYsoltar() {
        textArea.setLineWrap(true);//configuración del textArea
        textArea.setWrapStyleWord(true);
        textArea.setAutoscrolls(true);
        textArea.setMargin(new Insets(10, 10, 10, 10));
        //gestiona la transferencia de datos
        textArea.setTransferHandler(new TransferHandler() {
            @Override
            public boolean canImport(TransferHandler.TransferSupport info) {
                return info.isDataFlavorSupported(DataFlavor.javaFileListFlavor);
            }

            @Override //maneja la lógica de importación de datos cuando se suelta el archivo TXT en el textArea
            public boolean importData(TransferHandler.TransferSupport info) {
                if (!canImport(info)) {
                    return false;
                }

                Transferable transferable = info.getTransferable();
                try {
                    Object transferData = transferable.getTransferData(DataFlavor.javaFileListFlavor);
                    if (transferData instanceof List) {
                        List<?> files = (List<?>) transferData;
                        for (Object fileObject : files) {
                            if (fileObject instanceof File) {
                                File file = (File) fileObject;
                                if (file.getName().endsWith(".txt")) {
                                    cargarTextoDesdeArchivo(file);
                                    return true;
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return false;
            }
        });
    }

private void cargarTextoDesdeArchivo(File file) {
    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {//lee el contenido del archivo
        StringBuilder contenidoParaTextArea = new StringBuilder();//varias áreas de texto
        StringBuilder primeraLineaParaJTextArea1 = new StringBuilder();
        StringBuilder segundaLineaParaJTextArea2 = new StringBuilder();
        StringBuilder cuartaLineaParaJTextArea3 = new StringBuilder();

        // se utiliza el método procesarContenidoDelArchivo para llenar las áreas de texto
        procesarContenidoDelArchivo(reader, contenidoParaTextArea, primeraLineaParaJTextArea1, segundaLineaParaJTextArea2, cuartaLineaParaJTextArea3);

        //contenido de las áreas de texto
        textArea.setText(contenidoParaTextArea.toString());
        jTextArea1.setText(primeraLineaParaJTextArea1.toString().trim());
        jTextArea2.setText(segundaLineaParaJTextArea2.toString().trim());
        jTextArea3.setText(cuartaLineaParaJTextArea3.toString().trim());

        // copiar el contenido de jTextArea2 a jTextArea4
        jTextArea4.setText("Estado    " + jTextArea2.getText().replaceAll("\n", "     "));

        // añado el contenido de jTextArea1 a jTextArea4 en líneas separadas
        String[] lineasJTextArea1 = jTextArea1.getText().split("\n");
        for (String linea : lineasJTextArea1) {
            jTextArea4.append("\n   " + linea.trim());
        }

    } catch (Exception e) {
        e.printStackTrace();
    }
}

private void procesarContenidoDelArchivo(BufferedReader reader, StringBuilder contenidoParaTextArea,
     StringBuilder primeraLineaParaJTextArea1, StringBuilder segundaLineaParaJTextArea2,
    StringBuilder cuartaLineaParaJTextArea3) throws IOException {
    String linea;     ////parámetros que se pasan al método StringBuilder
    int numeroDeLinea = 0;

    while ((linea = reader.readLine()) != null) {
        contenidoParaTextArea.append(linea).append("\n"); //agregar a stringBuilder
                    
        switch (numeroDeLinea) { //un switch para determinar la linea actual
            case 0 -> procesarLinea(linea, primeraLineaParaJTextArea1);
            case 1 -> procesarLinea(linea, segundaLineaParaJTextArea2);
            case 3 -> procesarLinea(linea, cuartaLineaParaJTextArea3);
            default -> {
            }
        }

        numeroDeLinea++; // el contador de linea XD
    }
}

private void procesarLinea(String linea, StringBuilder stringBuilder) {
    int indiceInicio = linea.indexOf('{'); //buscan en la linea{
    int indiceFin = linea.indexOf('}');//buscan en la linea {
    if (indiceInicio != -1 && indiceFin != -1) {
        String letras = linea.substring(indiceInicio + 1, indiceFin);//extraer las letras 
        String[] palabras = letras.split(","); //
        for (String palabra : palabras) {
            stringBuilder.append(palabra.trim()).append("\n");//se agrega a StringBuilder y FIN XD
        }
    }
}
}