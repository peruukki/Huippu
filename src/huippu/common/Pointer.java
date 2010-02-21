package huippu.common;

import java.io.IOException;

public abstract class Pointer
    extends DromeComponent
    implements IStorable
{
    protected static final int LINE_THICKNESS = 2;

    public final byte[] getAsBytes()
        throws IOException
    {
        final byte[] dataX = Storable.getDataByte( mCell.x );
        final byte[] dataY = Storable.getDataByte( mCell.y );
        
        // Append all data to a continuous byte array
        final byte[] data = new byte[   dataX.length
                                      + dataY.length ];
        
        int offset = 0;
        offset = Storable.appendData( dataX, data, offset );
        offset = Storable.appendData( dataY, data, offset );
        return data;
    }
}
