package huippu.common;

import java.io.DataInputStream;
import java.io.IOException;



public final class Score
    implements IStorable
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
    
    public byte[] getAsBytes()
        throws IOException
    {
        // Convert all values to byte data
        final byte[] dataValue = Storable.getDataInt( mValue );
        final byte[] dataLevel = Storable.getDataInt( mLevel );
        final byte[] dataDate = mDate.getAsBytes();
        
        // Append all data to a continuous byte array
        final byte[] data = new byte[   dataValue.length
                                      + dataLevel.length
                                      + dataDate.length ];
        
        int offset = 0;
        
        offset = Storable.appendData( dataValue, data, offset );
        offset = Storable.appendData( dataLevel, data, offset );
        offset = Storable.appendData( dataDate, data, offset );
        
        return data;
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
    
    private static final Score[] mScoresTotal = new Score[]
    {
        new Score( 512, 5, new ScoreDate( 20,  2, 10 ) ),
        new Score( 425, 4, new ScoreDate( 19,  2, 10 ) ),
        new Score( 418, 4, new ScoreDate(  9,  2, 10 ) ),
        new Score( 400, 4, new ScoreDate( 12,  2, 10 ) ),
        new Score( 390, 4, new ScoreDate( 19,  2, 10 ) ),
    };
    
    private static final Score[] mScoresLevel = new Score[]
    {
        new Score( 133, 1, new ScoreDate(  9,  2, 10 ) ),
        new Score( 125, 1, new ScoreDate( 10,  2, 10 ) ),
        new Score( 124, 1, new ScoreDate( 19,  2, 10 ) ),
        new Score( 124, 1, new ScoreDate( 10,  2, 10 ) ),
        new Score( 121, 1, new ScoreDate( 13,  2, 10 ) ),
    };
    
    private static final Score[] mRemovesLevel = new Score[]
    {
        new Score(  7,  1, new ScoreDate(  9,  2, 10 ) ),
        new Score( 10,  1, new ScoreDate( 19,  2, 10 ) ),
        new Score( 10,  1, new ScoreDate( 10,  2, 10 ) ),
        new Score( 11,  1, new ScoreDate( 13,  2, 10 ) ),
        new Score( 12,  1, new ScoreDate( 19,  2, 10 ) ),
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
