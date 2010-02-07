package huippu.common;

public final class StoreIntDescending
    extends StoreInt
{
    public StoreIntDescending( final int[] pInitialValues )
    {
        super( pInitialValues );
    }

    protected final int getIndexToAdd( final int pValue )
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
 }
