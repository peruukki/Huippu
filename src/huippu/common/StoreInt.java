package huippu.common;

public abstract class StoreInt
{
    final protected int[] mValues;
    
    public StoreInt( final int[] pInitialValues )
    {
        mValues = pInitialValues;
    }
    
    public final boolean addValue( final int pValue )
    {
        final int index = getIndexToAdd( pValue );
        if ( index != -1 )
        {
            addToIndex( pValue, index );
        }
        return index != -1;
    }
    
    public final int[] getValues()
    {
        return mValues;
    }
    
    public final void setValues( final int[] pValues )
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
    
    private final void addToIndex( final int pValue, final int pIndex )
    {
        for ( int i = mValues.length - 1; i > pIndex; i-- )
        {
            mValues[ i ] = mValues[ i - 1 ];
        }
        mValues[ pIndex ] = pValue;
    }
    
    protected abstract int getIndexToAdd( final int pValue );
}
