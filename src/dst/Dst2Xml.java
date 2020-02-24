package dst;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

public class Dst2Xml extends AbstractDst2Xml
{

    public static void main( String[] args ) throws IOException
    {

        Properties prop = new Properties();
        prop.load( new FileReader( "Dst2Xml.properties" ) );

        String inputFile = prop.getProperty( "fileIn" );
        String outputFile = prop.getProperty( "fileOut" );
        String searchFor = prop.getProperty( "searchFor" );
        String replaceWith = prop.getProperty( "replaceWith" );

        System.out.println( "Dst2Xml Ver. 0.1" );
        System.out.println();
        System.out.println( "Parametres:");
        System.out.println( "\tfileIn = " + inputFile );
        System.out.println( "\tfileOut = " + outputFile );
        System.out.println( "\tsearchFor = " + searchFor );
        System.out.println( "\treplaceWith = " + replaceWith );
        System.out.println();
        System.out.println( "Process:");
        
        
        String xml = readDstFile( inputFile );
        writeXmlFile( inputFile + ".original.xml", xml );
        xml = replaceValues( xml, searchFor, replaceWith );
        writeXmlFile( inputFile + ".replaced.xml", xml );
        writeDstFile( outputFile, xml );
    }

    private static String replaceValues( String xml, String searchFor, String replaceWith )
    {
        xml = xml.replace( ">" + searchFor, ">" + replaceWith );
        xml = xml.replace( searchFor.substring( 2 ), replaceWith.substring( 2 ) );
        xml = fixTildedChars( xml );
        return xml;
    }
    
    private static String fixTildedChars( String xml ) {
        return xml.replace( "Ã","Ó");
    }

    private static void writeXmlFile( String filename, String xml ) throws IOException
    {
        System.out.println( "\tWriting file: " + filename );

        File f = new File( filename );
        FileWriter fis = new FileWriter( f );

        fis.write( xml.replace( "<AcS", "\n<AcS" ) );
        fis.flush();
        fis.close();

    }

    public static String readDstFile( String filename ) throws IOException
    {
        System.out.println( "\tReading file: " + filename );
        File f = new File( filename );
        FileInputStream fis = new FileInputStream( f );

        StringBuilder sb = new StringBuilder();

        byte[] buffer = new byte[512];
        int len;
        while ( ( len = fis.read( buffer ) ) > 0 )
        {
            sb.append( decode( buffer, len ) );
        }

        fis.close();

        return fixTildedChars( sb.toString() );
    }

    public static void writeDstFile( String filename, String xml ) throws IOException
    {
        System.out.println( "\tWriting file: " + filename );

        File o = new File( filename );
        FileOutputStream w = new FileOutputStream( o );

        for ( byte b : xml.getBytes() )
        {
            w.write( encode( b ) );
        }

        w.flush();
        w.close();
    }

}
