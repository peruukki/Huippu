package huippu.common;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;



public final class Score
{
    private static final boolean mUseAllZero = true;
    
    private final int mValue;
    private final int mLevel;
    
    public Score()
    {
        this( 0, 0 );
    }
    
    public Score( final int pValue, final int pLevel )
    {
        mValue = pValue;
        mLevel = pLevel;
    }
    
    public Score( final DataInputStream pInput )
        throws IOException
    {
        mValue = pInput.readInt();
        mLevel = pInput.readInt();
    }
    
    public int getValue()
    {
        return mValue;
    }
    
    public int getLevel()
    {
        return mLevel;
    }
    
    public byte[] getAsBytes()
        throws IOException
    {
        final byte[] dataValue = getDataInt( mValue );
        final byte[] dataLevel = getDataInt( mLevel );
        final byte[] data = new byte[   dataValue.length
                                      + dataLevel.length ];
        System.arraycopy( dataValue, 0, data, 0, dataValue.length );
        System.arraycopy( dataLevel, 0, data, dataValue.length,
                          dataLevel.length );
        return data;
    }
    
    private static final byte[] getDataInt( final int pValue )
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
    
    private static final Score[] mScoresTotal = new Score[]
    {
        new Score( 50, 1 ),
        new Score( 40, 1 ),
        new Score( 30, 1 ),
        new Score( 0, 0 ),
        new Score( 0, 0 )
    };
    
    private static final Score[] mScoresLevel = new Score[]
    {
        new Score( 20, 1 ),
        new Score( 19, 1 ),
        new Score( 18, 1 ),
        new Score( 0, 0 ),
        new Score( 0, 0 )
    };
    
    private static final Score[] mRemovesLevel = new Score[]
    {
        new Score( 11, 1 ),
        new Score( 12, 1 ),
        new Score( 13, 1 ),
        new Score( 14, 1 ),
        new Score( 0, 0 )
    };
    
    static final Score[] getInitialScoresTotal()
    {
        if ( mUseAllZero )
        {
            return getEmptyScores( mScoresTotal.length );
        }
        else
        {
            return mScoresTotal;
        }
    }
    
    static final Score[] getInitialScoresLevel()
    {
        if ( mUseAllZero )
        {
            return getEmptyScores( mScoresLevel.length );
        }
        else
        {
            return mScoresLevel;
        }
    }
    
    static final Score[] getInitialRemovesLevel()
    {
        if ( mUseAllZero )
        {
            return getEmptyScores( mRemovesLevel.length );
        }
        else
        {
            return mRemovesLevel;
        }
    }
    
    static final Score[] getEmptyScores( final int pSize )
    {
        final Score[] scores = new Score[ pSize ];
        for ( int i = 0; i < scores.length; i++ )
        {
            scores[ i ] = new Score();
        }
        return scores;
    }
}
