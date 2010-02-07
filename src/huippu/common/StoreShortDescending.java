package huippu.common;

public final class StoreShortDescending
{
    final protected short[] mValues;
    
    public StoreShortDescending( final short[] pInitialValues )
    {
        mValues = pInitialValues;
    }
    
    public final boolean addValue( final short pValue )
    {
        final int index = getIndexToAdd( pValue );
        if ( index != -1 )
        {
            addToIndex( pValue, index );
        }
        return index != -1;
    }
    
    public final short[] getValues()
    {
        return mValues;
    }
    
    public final void setValues( final short[] pValues )
    {
        for ( int i = 0; i < mValues.length; i++ )
        {
            mValues[ i ] = pValues[ i ];
        }
    }
    
    public final void clear()
    {
        for ( int i = 0; i < mValues.length; i++ )
        {
            mValues[ i ] = 0;
        }
    }
    
    public final int size()
    {
        return mValues.length;
    }
   
    private final int getIndexToAdd( final short pValue )
    {
        int index = 0;

        for ( int i = mValues.length - 1; index == 0 && i >= 0; i-- )
        {
            if ( pValue < mValues[ i ] )
            {
                index = i + 1;
            }
        }

        return ( index == mValues.length ) ? -1 : index;
    }
    
    private final void addToIndex( final short pValue, final int pIndex )
    {
        for ( int i = mValues.length - 1; i > pIndex; i-- )
        {
            mValues[ i ] = mValues[ i - 1 ];
        }
        mValues[ pIndex ] = pValue;
    }
 }
