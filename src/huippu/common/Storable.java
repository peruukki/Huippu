package huippu.common;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public abstract class Storable
{
    public static final byte[] getDataInt( final int pValue )
        throws IOException
    {
        final byte[] data;
        
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final DataOutputStream dos = new DataOutputStream( baos );
        dos.writeInt( pValue );
        data = baos.toByteArray();
        dos.close();
        
        return data;
    }
    
    public static final byte[] getDataByte( final int pValue )
    {
        final byte[] data = new byte[ 1 ];
        data[ 0 ] = (byte) pValue;
        return data;
    }
    
    public static final int appendData( final byte[] pData,
                                        final byte[] pBuffer,
                                        final int pBufferOffset )
    {
        System.arraycopy( pData, 0, pBuffer, pBufferOffset, pData.length );
        return pBufferOffset + pData.length;
    }
}
