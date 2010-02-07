package huippu.common;

public class StoreIntAscending
    extends StoreInt
{
    public StoreIntAscending( final int[] pInitialValues )
    {
        super( pInitialValues );
    }

    protected final int getIndexToAdd( final int pValue )
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
}
