package huippu.common;

public class StoreByteAscending
{
    final protected byte[] mValues;
    
    public StoreByteAscending( final byte[] pInitialValues )
    {
        mValues = pInitialValues;
    }
    
    public final boolean addValue( final byte pValue )
    {
        final int index = getIndexToAdd( pValue );
        if ( index != -1 )
        {
            addToIndex( pValue, index );
        }
        return index != -1;
    }
    
    public final void setValues( final byte[] pValues )
    {
        for ( int i = 0; i < mValues.length; i++ )
        {
            mValues[ i ] = pValues[ i ];
        }
    }
    
    public final byte[] getValues()
    {
        return mValues;
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
   
    private final int getIndexToAdd( final byte pValue )
    {
        int index = -1;

        if ( pValue != 0 )
        {
            index = 0;
            for ( int i = mValues.length - 1; index == 0 && i >= 0; i-- )
            {
                if (    mValues[ i ] != 0
                     && pValue > mValues[ i ] )
                {
                    index = i + 1;
                }
            }
            index = ( index == mValues.length ) ? -1 : index;
        }

        return index;
    }
    
    private final void addToIndex( final byte pValue, final int pIndex )
    {
        for ( int i = mValues.length - 1; i > pIndex; i-- )
        {
            mValues[ i ] = mValues[ i - 1 ];
        }
        mValues[ pIndex ] = pValue;
    }
}
