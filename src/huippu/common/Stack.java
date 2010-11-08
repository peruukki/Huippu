package huippu.common;

public final class Stack
{
    private final Object[] mItems;
    private int mItemCount = 0;
    
    public Stack( final int pMaxSize )
    {
        mItems = new Object[ pMaxSize ];
    }
    
    public Stack( final int pMaxSize, final Stack pInitialStack )
    {
        this( pMaxSize );
        if ( pInitialStack != null )
        {
            System.arraycopy( pInitialStack.mItems, 0, mItems, 0,
                              pInitialStack.mItemCount );
            mItemCount = pInitialStack.mItemCount;
        }
    }
    
    public final boolean push( final Object pItem )
    {
        boolean added = false;
        
        if ( mItemCount < mItems.length )
        {
            mItems[ mItemCount++ ] = pItem;
            added = true;
        }
        
        return added;
    }
    
    public final Object pop()
    {
        Object lastItem = null;
        
        if ( mItemCount > 0 )
        {
            lastItem = mItems[ mItemCount - 1 ];
            mItems[ --mItemCount ] = null;
        }
        
        return lastItem;
    }
    
    public final int getItemCount()
    {
        return mItemCount;
    }
    
    public final boolean isEmpty()
    {
        return mItemCount == 0;
    }
}
