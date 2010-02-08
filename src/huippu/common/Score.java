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
    private final ScoreDate mDate;
    
    public Score()
    {
        this( 0, 0, new ScoreDate() );
    }
    
    public Score( final int pValue, final int pLevel, final ScoreDate pDate )
    {
        mValue = pValue;
        mLevel = pLevel;
        mDate = pDate;
    }
    
    public Score( final DataInputStream pInput )
        throws IOException
    {
        mValue = pInput.readInt();
        mLevel = pInput.readInt();
        mDate = new ScoreDate( pInput.readByte(),
                               pInput.readByte(),
                               pInput.readByte() );
    }
    
    public int getValue()
    {
        return mValue;
    }
    
    public int getLevel()
    {
        return mLevel;
    }
    
    public ScoreDate getDate()
    {
        return mDate;
    }
    
    public byte[] getAsBytes()
        throws IOException
    {
        final byte[] dataValue = getDataInt( mValue );
        final byte[] dataLevel = getDataInt( mLevel );
        final byte[] dataDate = mDate.getAsBytes();
        final byte[] data = new byte[   dataValue.length
                                      + dataLevel.length
                                      + dataDate.length ];
        int offset = 0;
        System.arraycopy( dataValue, 0, data, offset, dataValue.length );
        offset += dataValue.length;
        System.arraycopy( dataLevel, 0, data, offset, dataLevel.length );
        offset += dataLevel.length;
        System.arraycopy( dataDate, 0, data, offset, dataDate.length );
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
        new Score(   50,  1, new ScoreDate(  2,  2, 2010 ) ),
        new Score(   40,  1, new ScoreDate(  2,  2, 2010 ) ),
        new Score(   30,  1, new ScoreDate(  5,  2, 2010 ) ),
        new Score(),
        new Score()
    };
    
    private static final Score[] mScoresLevel = new Score[]
    {
        new Score(  20,  1, new ScoreDate(  1,  1, 2010 ) ),
        new Score(  19,  1, new ScoreDate( 30,  1, 2010 ) ),
        new Score(  18,  1, new ScoreDate( 22, 12, 2009 ) ),
        new Score(),
        new Score()
    };
    
    private static final Score[] mRemovesLevel = new Score[]
    {
        new Score( 11,  1, new ScoreDate(  2,  2, 2010 ) ),
        new Score( 12,  1, new ScoreDate(  2,  2, 2010 ) ),
        new Score( 13,  1, new ScoreDate(  1, 11, 2009 ) ),
        new Score( 14,  1, new ScoreDate(  9,  2, 2010 ) ),
        new Score()
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
