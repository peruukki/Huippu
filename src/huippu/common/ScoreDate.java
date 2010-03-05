package huippu.common;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

public final class ScoreDate
{
    final byte mDay;
    final byte mMonth;
    final byte mYear;
    
    public ScoreDate()
    {
        this( new Date() );
    }
    
    public ScoreDate( final Date pDate )
    {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime( pDate );
        mDay = (byte) calendar.get( Calendar.DATE );
        mMonth = (byte) ( calendar.get( Calendar.MONTH ) + 1 );
        mYear = (byte) ( calendar.get( Calendar.YEAR ) % 100 );
    }
    
    public ScoreDate( final int pDay, final int pMonth, final int pYear )
    {
        mDay = (byte) pDay;
        mMonth = (byte) pMonth;
        mYear = (byte) pYear;
    }
    
    public final byte[] getAsBytes()
        throws IOException
    {
        final byte[] data;
        
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final DataOutputStream dos = new DataOutputStream( baos );
        dos.writeByte( mDay );
        dos.writeByte( mMonth );
        dos.writeByte( mYear );
        data = baos.toByteArray();
        dos.close();
        
        return data;
    }
    
    public final String toString()
    {
        return   format( mDay ) + Resources.DATE_DELIM
               + format( mMonth ) + Resources.DATE_DELIM
               + format( mYear );
    }
    
    private static final String format( final byte pValue )
    {
        final String valueStr;
        if ( pValue < 10 )
        {
            valueStr = "0" + pValue;
        }
        else
        {
            valueStr = String.valueOf( pValue );
        }
        return valueStr;
    }
}
