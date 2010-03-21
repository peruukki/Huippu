package huippu.common;

public abstract class ScoresInt
{
    final protected Score[] mValues;
    
    public ScoresInt( final Score[] pInitialValues )
    {
        mValues = pInitialValues;
    }
    
    public final boolean addValue( final Score pValue )
    {
        final int index = getIndexToAdd( pValue );
        if ( index != -1 )
        {
            addToIndex( pValue, index );
        }
        return index != -1;
    }
    
    public final Score[] getValues()
    {
        return mValues;
    }
    
    public final void setValues( final Score[] pValues )
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
            mValues[ i ] = new Score();
        }
    }
    
    public final int size()
    {
        return mValues.length;
    }
    
    private final void addToIndex( final Score pValue, final int pIndex )
    {
        for ( int i = mValues.length - 1; i > pIndex; i-- )
        {
            mValues[ i ] = mValues[ i - 1 ];
        }
        mValues[ pIndex ] = pValue;
    }
    
    protected abstract int getIndexToAdd( final Score pValue );
}
