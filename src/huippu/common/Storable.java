package huippu.common;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public abstract class Storable
{
    public Storable()
    {
        // Does nothing
    }
    
    public Storable( final DataInputStream pInput )
        throws IOException
    {
        // Does nothing
    }
    
    public abstract byte[] getAsBytes()
        throws IOException;
    
    protected static final byte[] getDataInt( final int pValue )
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
}
