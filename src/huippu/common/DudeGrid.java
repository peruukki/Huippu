package huippu.common;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Random;

public abstract class DudeGrid
    implements IStorable
{
    private static final int INVALID_COLUMN = -1;
    
    protected final int mColumnCount;
    protected final int mRowCount;
    
    private final int mDudeCount;

    private static final double REDUCTION_COEFF_BASIC = 0.1;
    private static final double REDUCTION_COEFF_AFTER_CHANGE_LIMIT = 0.005;
    private static final double REDUCTION_FACTOR_MAX = 0.995;
    private static final double REDUCTION_FACTOR_CHANGE_LIMIT = 0.9;
    private final int mProportionBasic;
    private int mProportionReduction = 0;
    private int mProportionAddition = 0;
    
    private static final int BASIC_MAX_RANDOM_VALUE = 1000;
    private final Random mRandom = new Random();
    private final int mMaxRandomValue;
    
    protected final Dude[][] mDudes;
    protected boolean mIsGridEmpty = false;
    
    protected Stack mMovingDudes;
    
    protected final boolean[][] mRemovable;
    protected int mRemovableFirstCol = INVALID_COLUMN;
    protected int mRemovableLastCol = INVALID_COLUMN;
    
    private Cell mCurrentCell = null;
    
    public DudeGrid( final int pColumnCount, final int pRowCount,
                     final int pDudeCount )
    {
        mColumnCount = pColumnCount;
        mRowCount = pRowCount;
        mDudeCount = pDudeCount;
        
        mDudes = new Dude[ mColumnCount ][ mRowCount ];
        mRemovable = new boolean[ mColumnCount ][ mRowCount ];
        
        mMaxRandomValue =   BASIC_MAX_RANDOM_VALUE
                          - ( BASIC_MAX_RANDOM_VALUE % mDudeCount );
        mProportionBasic = mMaxRandomValue / mDudeCount;
    }
    
    public DudeGrid( final int pDudeCount, final DataInputStream pInput )
        throws IOException
    {
        this( pInput.readByte(), pInput.readByte(), pDudeCount );
    }
    
    public final byte[] getAsBytes()
        throws IOException
    {
        // Convert all values to byte data
        final byte[] dataColumnCount = Storable.getDataByte( mColumnCount );
        final byte[] dataRowCount = Storable.getDataByte( mRowCount );

        final byte[] dataGrid = new byte[ mColumnCount * mRowCount ];
        for ( int x = 0; x < mColumnCount; x++ )
        {
            for ( int y = 0; y < mRowCount; y++ )
            {
                int id = Dude.INVALID_ID;
                final Dude dude = mDudes[ x ][ y ]; 
                if ( dude != null )
                {
                    id = dude.getId();
                }
                dataGrid[ ( x * mRowCount ) + y ] = (byte) id;
            }
        }
        
        // Append all data to a continuous byte array
        final byte[] data = new byte[   dataColumnCount.length
                                      + dataRowCount.length
                                      + dataGrid.length ];
        
        int offset = 0;
        
        offset = Storable.appendData( dataColumnCount, data, offset );
        offset = Storable.appendData( dataRowCount, data, offset );
        
        offset = Storable.appendData( dataGrid, data, offset );
        
        return data;
    }    

    public abstract void fillWithDudes( final int pLevel );

    public abstract void drawDudesAll( final Object pGraphics );
    
    public final void setCurrentCell( final Cell pCell )
    {
        mCurrentCell = pCell;
        updateRemovable();
    }
    
    private final void updateRemovable()
    {
        clearRemovable();
        setRemovable( mCurrentCell );
    }
    
    public final boolean moveDudes()
    {
        final Stack newMovingDudes =
            new Stack( mMovingDudes.getItemCount() );

        while ( !mMovingDudes.isEmpty() )
        {
            final Dude dude = (Dude) mMovingDudes.pop();
            if (    !dude.isRemoved()
                 && dude.move() )
            {
                newMovingDudes.push( dude );
            }
        }
        
        mMovingDudes = newMovingDudes;
        return !mMovingDudes.isEmpty();
    }

    public final int removeDudes()
    {
        int removeCount = 0;
        if ( canRemoveDudesNow() )
        {
            removeCount = removeDudesFromGrid();
            mIsGridEmpty = moveDudesAfterRemove();
            updateRemovable();
        }
        return removeCount;
    }
    
    public final void updateDudePositions()
    {
        for ( int x = 0; x < mColumnCount; x++ )
        {
            for ( int y = 0; y < mRowCount; y++ )
            {
                final Dude dude = mDudes[ x ][ y ]; 
                if ( dude != null )
                {
                    dude.updateScreenPosition();
                }
            }
        }
    }
    
    public final boolean allDudesRemoved()
    {
        return mIsGridEmpty;
    }
    
    public final boolean isPossibleToRemoveDudes()
    {
        boolean removableDudesExist = canRemoveDudesNow();
        if ( !removableDudesExist )
        {
            boolean foundRemovable = false;
            
            for ( int x = 0;
                     !foundRemovable
                  && ( x < mColumnCount - 1 )
                  && !isColumnEmpty( x );
                  x++ )
            {
                for ( int y = mRowCount - 1;
                         !foundRemovable
                      && y > 0
                      && ( mDudes[ x ][ y ] != null );
                      y-- )
                {
                    // Check dude on the right
                    foundRemovable = mDudes[ x ][ y ].equals( mDudes[ x + 1 ][ y ] );

                    // Check dude above
                    foundRemovable |= mDudes[ x ][ y ].equals( mDudes[ x ][ y - 1 ] );
                }
            }
            
            removableDudesExist = foundRemovable;
        }        
        return removableDudesExist;
    }

    private final boolean canRemoveDudesNow()
    {
        return mRemovableFirstCol != INVALID_COLUMN;
    }
    
    private final int removeDudesFromGrid()
    {
        int removeCount = 0;
        
        for ( int x = mRemovableFirstCol; x <= mRemovableLastCol; x++ )
        {
            for ( int y = mRowCount - 1; y >= 0; y-- )
            {
                if ( mRemovable[ x ][ y ] )
                {
                    mDudes[ x ][ y ].setRemoved();
                    mDudes[ x ][ y ] = null;
                    removeCount++;
                }
            }
        }
        
        return removeCount;
    }
    
    private final boolean moveDudesAfterRemove()
    {
        mMovingDudes = new Stack( mColumnCount * mRowCount, mMovingDudes );
        moveDudesDown();
        return moveDudesLeft();
    }
    
    private final void moveDudesDown()
    {
        for ( int x = mRemovableFirstCol; x < mColumnCount; x++ )
        {
            int moveCount = 0;
            for ( int y = mRowCount - 1; y >= 0; y-- )
            {
                final Dude dude = mDudes[ x ][ y ];
                if ( dude == null )
                {
                    moveCount++;
                }
                else if ( moveCount > 0 )
                {
                    if ( !dude.isMoving() )
                    {
                        mMovingDudes.push( dude );
                    }
                    moveDudeDown( dude, moveCount );
                }
            }
        }
    }

    private final boolean moveDudesLeft()
    {
        boolean allColumnsEmpty = true;
        
        int moveCount = 0;
        for ( int x = 0; x < mColumnCount; x++ )
        {
            if ( isColumnEmpty( x ) )
            {
                moveCount++;
            }
            else
            {
                allColumnsEmpty = false;
                if ( moveCount > 0 )
                {
                    moveColumnLeft( x, moveCount );
                }
            }
        }
        
        return allColumnsEmpty;
    }
    
    private final boolean isColumnEmpty( final int pColumnX )
    {
        return ( mDudes[ pColumnX ][ mRowCount - 1 ] == null );
    }
    
    private final void moveColumnLeft( final int pColumnX, final int pCount )
    {
        boolean noDudesLeft = false;        
        for ( int y = mRowCount - 1; !noDudesLeft && y >= 0; y-- )
        {
            final Dude dude = mDudes[ pColumnX ][ y ];
            noDudesLeft = ( dude == null ); 
            if ( !noDudesLeft )
            {
                if ( !dude.isMoving() )
                {
                    mMovingDudes.push( dude );
                }
                moveDudeLeft( dude, pCount );
            }
        }
    }
    
    private final void moveDudeDown( final Dude pDude, final int pCount )
    {
        final Cell cell = pDude.getCell();
        mDudes[ cell.x ][ cell.y + pCount ] = pDude;
        mDudes[ cell.x ][ cell.y ] = null;
        pDude.moveDown( pCount );
    }
    
    private final void moveDudeLeft( final Dude pDude, final int pCount )
    {
        final Cell cell = pDude.getCell();
        mDudes[ cell.x - pCount ][ cell.y ] = pDude;
        mDudes[ cell.x ][ cell.y ] = null;
        pDude.moveLeft( pCount );
    }
    
    private final void clearRemovable()
    {
        for ( int x = 0; x < mColumnCount; x++ )
        {
            for ( int y = 0; y < mRowCount; y++ )
            {
                mRemovable[ x ][ y ] = false;
            }
        }
        mRemovableFirstCol = INVALID_COLUMN;
        mRemovableLastCol = INVALID_COLUMN;
    }
    
    private final void setRemovable( final Cell pCell )
    {
        final Dude currentDude = mDudes[ pCell.x ][ pCell.y ];
        if ( currentDude != null )
        {
            // Check Dudes above
            int otherY = pCell.y - 1;
            if (    otherY >= 0
                    && !mRemovable[ pCell.x ][ otherY ] )
            {
                setRemovable( currentDude, new Cell( pCell.x, otherY ) );
            }

            // Check Dudes below
            otherY = pCell.y + 1;
            if (    otherY < mRowCount
                    && !mRemovable[ pCell.x ][ otherY ] )
            {
                setRemovable( currentDude, new Cell( pCell.x, otherY ) );
            }

            // Check Dudes on the left
            int otherX = pCell.x - 1;
            if (    otherX >= 0
                    && !mRemovable[ otherX ][ pCell.y ] )
            {
                setRemovable( currentDude, new Cell( otherX, pCell.y ) );
            }

            // Check Dudes on the right
            otherX = pCell.x + 1;
            if (    otherX < mColumnCount
                    && !mRemovable[ otherX ][ pCell.y ] )
            {
                setRemovable( currentDude, new Cell( otherX, pCell.y ) );
            }
        }
    }
    
    private final void setRemovable( final Dude pDude,
                                     final Cell pOtherCell )
    {
        final Dude otherDude = mDudes[ pOtherCell.x ][ pOtherCell.y ];
        if ( pDude.equals( otherDude ) )
        {
            final Cell currentCell = pDude.getCell();
            
            mRemovable[ currentCell.x ][ currentCell.y ] = true;
            mRemovable[ pOtherCell.x ][ pOtherCell.y ] = true;
            
            updateFirstAndLastRemovableColumn( currentCell.x, pOtherCell.x );
            
            setRemovable( pOtherCell );
        }
    }
    
    private final void updateFirstAndLastRemovableColumn( final int pX1,
                                                          final int pX2 )
    {
        final int smaller = ( pX1 < pX2 ) ? pX1 : pX2;
        final int larger = ( pX1 >= pX2 ) ? pX1 : pX2;
        
        if (    mRemovableFirstCol == INVALID_COLUMN
             || smaller < mRemovableFirstCol )
        {
            mRemovableFirstCol = smaller;
        }
        
        if (    mRemovableLastCol == INVALID_COLUMN
             || larger > mRemovableLastCol )
        {
            mRemovableLastCol = larger;
        }
    }
    
    protected final void calculateDudeIdProbabilities( final int pLevel )
    {
        final int levelCoeff = pLevel - 1;
        double coeff = levelCoeff * REDUCTION_COEFF_BASIC;
        if ( coeff > REDUCTION_FACTOR_CHANGE_LIMIT )
        {
            coeff =   REDUCTION_FACTOR_CHANGE_LIMIT
                    + ( levelCoeff * REDUCTION_COEFF_AFTER_CHANGE_LIMIT );
            if ( coeff >= 1.0 )
            {
                // Make sure we don't exceed the maximum limit
                coeff = REDUCTION_FACTOR_MAX;
            }
        }
        mProportionReduction = (int) ( coeff * mProportionBasic );
        mProportionAddition = ( mProportionReduction / ( mDudeCount - 1 ) );
    }
    
    protected final int getNextDudeId( final int pX, final int pY )
    {
        // Initialize all Dudes equally probable
        final int[] proportions = getBasicProportions();
        
        // Adjust probabilities based on neighbor cells
        updateProportions( proportions, getNeighborIds( pX, pY ) );
        
        // Get the Dude ID
        return getId( proportions, getRandomValue( mMaxRandomValue ) );
    }
    
    private final int[] getBasicProportions()
    {
        final int[] proportions = new int[ mDudeCount ];
        for ( int i = 0; i < proportions.length; i++ )
        {
            proportions[ i ] = mProportionBasic;
        }
        return proportions;
    }
    
    private final int[] getNeighborIds( final int pX, final int pY )
    {
        final int[] neighborIds = new int[ 2 ];
        
        neighborIds[ 0 ] = Dude.INVALID_ID;
        if ( pX > 0 )
        {
            neighborIds[ 0 ] = mDudes[ pX - 1 ][ pY ].getId();
        }
        
        neighborIds[ 1 ] = Dude.INVALID_ID;
        if ( pY > 0 )
        {
            neighborIds[ 1 ] = mDudes[ pX ][ pY - 1 ].getId();
        }
        
        return neighborIds;
    }
    
    private final void updateProportions( final int[] pProportions,
                                          final int[] pNeighborIds )
    {
        for ( int i = 0; i < pNeighborIds.length; i++ )
        {
            if ( pNeighborIds[ i ] != Dude.INVALID_ID )
            {
                for ( int j = 0; j < pProportions.length; j++ )
                {
                    if ( j == pNeighborIds[ i ] )
                    {
                        pProportions[ j ] -= mProportionReduction;
                    }
                    else
                    {
                        pProportions[ j ] += mProportionAddition;
                    }
                }
            }
        }
    }
    
    private final int getRandomValue( final int pMaxValue )
    {
        int randomValue = ( mRandom.nextInt() % pMaxValue );
        if ( randomValue < 0 )
        {
            randomValue += pMaxValue;
        }
        return randomValue;
    }
    
    private final int getId( final int[] pProportions, final int pRandomValue )
    {
        int id = Dude.INVALID_ID;
        
        int lowerLimit = 0;
        for ( int i = 0;
              id == Dude.INVALID_ID && i < pProportions.length;
              i++ )
        {
            if ( pRandomValue < ( lowerLimit + pProportions[ i ] ) )
            {
                id = i;
            }
            else
            {
                lowerLimit += pProportions[ i ];
            }
        }
        
        // Handle the special case where the random value doesn't fall into any
        // ID range because of rounding off of integers
        if ( id == Dude.INVALID_ID )
        {
            id = getRandomValue( mDudeCount );
        }
        
        return id;
    }
}
