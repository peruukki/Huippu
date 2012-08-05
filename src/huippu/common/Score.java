package huippu.common;

import java.io.DataInputStream;
import java.io.IOException;

public final class Score
    implements IStorable
{
    private static final boolean USE_ALL_ZERO = false;
    
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

    public Score( final int pValue, final int pLevel )
    {
        mValue = pValue;
        mLevel = pLevel;
        mDate = new ScoreDate();
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
    
    private static final Score[] SCORES_TOTAL_BIGGEST = new Score[]
    {
        new Score( 1074, 11, new ScoreDate( 24,  9, 10 ) ),
        new Score(  895,  9, new ScoreDate(  4,  5, 12 ) ),
        new Score(  714,  7, new ScoreDate( 20,  3, 10 ) ),
        new Score(  706,  8, new ScoreDate( 21,  8, 10 ) ),
        new Score(  693,  7, new ScoreDate(  2,  5, 12 ) ),
        new Score(),
        new Score(),
        new Score(),
        new Score(),
        new Score(),
    };
    
    private static final Score[] SCORES_TOTAL_SMALLEST = new Score[]
    {
        new Score( 55, 1, new ScoreDate(  5,  9, 10 ) ),
        new Score( 68, 1, new ScoreDate( 24,  3, 12 ) ),
        new Score( 68, 1, new ScoreDate( 11, 11, 10 ) ),
        new Score( 68, 1, new ScoreDate(  7,  7, 10 ) ),
        new Score( 69, 1, new ScoreDate( 27,  9, 10 ) ),
        new Score(),
        new Score(),
        new Score(),
        new Score(),
        new Score(),
    };
    
    private static final Score[] SCORES_LEVEL_BIGGEST = new Score[]
    {
        new Score( 133, 1, new ScoreDate(  9,  2, 10 ) ),
        new Score( 130, 1, new ScoreDate( 29, 10, 10 ) ),
        new Score( 127, 1, new ScoreDate(  9,  8, 11 ) ),
        new Score( 127, 1, new ScoreDate( 22,  2, 10 ) ),
        new Score( 127, 1, new ScoreDate( 22,  2, 10 ) ),
        new Score(),
        new Score(),
        new Score(),
        new Score(),
        new Score(),
    };
    
    private static final Score[] SCORES_LEVEL_SMALLEST = new Score[]
    {
        new Score( 71, 1, new ScoreDate( 21,  3, 10 ) ),
        new Score( 79, 6, new ScoreDate( 21,  8, 10 ) ),
        new Score( 79, 3, new ScoreDate( 21,  8, 10 ) ),
        new Score( 82, 1, new ScoreDate( 17,  8, 11 ) ),
        new Score( 82, 1, new ScoreDate( 24,  2, 11 ) ),
        new Score(),
        new Score(),
        new Score(),
        new Score(),
        new Score(),
    };
    
    private static final Score[] REMOVES_LEVEL_SMALLEST = new Score[]
    {
        new Score(  7,  1, new ScoreDate(  9,  2, 10 ) ),
        new Score(  8,  1, new ScoreDate( 29, 10, 10 ) ),
        new Score(  9,  1, new ScoreDate(  9,  8, 11 ) ),
        new Score(  9,  1, new ScoreDate( 22,  2, 10 ) ),
        new Score(  9,  1, new ScoreDate( 22,  2, 10 ) ),
        new Score(),
        new Score(),
        new Score(),
        new Score(),
        new Score(),
    };
    
    private static final Score[] REMOVES_LEVEL_BIGGEST = new Score[]
    {
        new Score( 25,  6, new ScoreDate( 21,  8, 10 ) ),
        new Score( 25,  3, new ScoreDate( 21,  8, 10 ) ),
        new Score( 24,  1, new ScoreDate( 17,  8, 11 ) ),
        new Score( 24,  1, new ScoreDate( 24,  2, 11 ) ),
        new Score( 24,  9, new ScoreDate( 24,  9, 10 ) ),
        new Score(),
        new Score(),
        new Score(),
        new Score(),
        new Score(),
    };
    
    private static final Score[] REMOVES_BIGGEST = new Score[]
    {
        new Score( 37,  1, new ScoreDate( 29, 10, 10 ) ),
        new Score( 32,  1, new ScoreDate( 17,  7, 12 ) ),
        new Score( 32,  1, new ScoreDate(  2,  5, 10 ) ),
        new Score( 31,  1, new ScoreDate( 22, 11, 10 ) ),
        new Score( 30,  1, new ScoreDate( 12,  3, 10 ) ),
        new Score(),
        new Score(),
        new Score(),
        new Score(),
        new Score(),
    };

    static final Score[] getInitialScoresTotalBiggest()
    {
        if ( USE_ALL_ZERO )
        {
            return getEmptyScores( SCORES_TOTAL_BIGGEST.length );
        }
        else
        {
            return SCORES_TOTAL_BIGGEST;
        }
    }

    static final Score[] getInitialScoresTotalSmallest()
    {
        if ( USE_ALL_ZERO )
        {
            return getEmptyScores( SCORES_TOTAL_SMALLEST.length );
        }
        else
        {
            return SCORES_TOTAL_SMALLEST;
        }
    }
    
    static final Score[] getInitialScoresLevelBiggest()
    {
        if ( USE_ALL_ZERO )
        {
            return getEmptyScores( SCORES_LEVEL_BIGGEST.length );
        }
        else
        {
            return SCORES_LEVEL_BIGGEST;
        }
    }
    
    static final Score[] getInitialScoresLevelSmallest()
    {
        if ( USE_ALL_ZERO )
        {
            return getEmptyScores( SCORES_LEVEL_SMALLEST.length );
        }
        else
        {
            return SCORES_LEVEL_SMALLEST;
        }
    }
    
    static final Score[] getInitialRemovesLevelSmallest()
    {
        if ( USE_ALL_ZERO )
        {
            return getEmptyScores( REMOVES_LEVEL_SMALLEST.length );
        }
        else
        {
            return REMOVES_LEVEL_SMALLEST;
        }
    }
    
    static final Score[] getInitialRemovesLevelBiggest()
    {
        if ( USE_ALL_ZERO )
        {
            return getEmptyScores( REMOVES_LEVEL_BIGGEST.length );
        }
        else
        {
            return REMOVES_LEVEL_BIGGEST;
        }
    }
    
    static final Score[] getInitialRemovesBiggest()
    {
        if ( USE_ALL_ZERO )
        {
            return getEmptyScores( REMOVES_BIGGEST.length );
        }
        else
        {
            return REMOVES_BIGGEST;
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
