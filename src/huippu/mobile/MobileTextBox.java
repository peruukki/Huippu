package huippu.mobile;

import huippu.common.Resources;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

final class MobileTextBox
{
    private static final int BORDER = 5;
    private static final int BOTTOM_REDUCTION = 2;
    
    private final int mLeftX;
    private final int mTopY;
    private final int mWidth;
    private final int mHeight;
    
    private final int mTextWidth;
    private final int mTextHeight;
    private final MobileText mText;
    
    public MobileTextBox( final String pText, final int pX, final int pY,
                          final Font pFont )
    {
        mTextWidth = getTextWidth( pFont, pText );
        mTextHeight = getTextHeight( pFont );
        
        mLeftX = pX - ( mTextWidth / 2 ) - BORDER;
        mTopY = pY - ( mTextHeight / 2 ) - BORDER;
        mWidth = mTextWidth + ( 2 * BORDER ) - 1;
        mHeight = mTextHeight + ( 2 * BORDER ) - 1 - BOTTOM_REDUCTION;
        
        mText = new MobileText( pText,
                                mLeftX + BORDER,
                                mTopY + BORDER -1,
                                Graphics.TOP | Graphics.LEFT,
                                pFont );
    }
    
    final int getLeftX()
    {
        return mLeftX;
    }
    
    final int getTopY()
    {
        return mTopY;
    }
    
    final int getWidth()
    {
        return mWidth;
    }
    
    final int getHeight()
    {
        return mHeight;
    }
    
    final void draw( final Graphics pG )
    {
        // Draw shadow
        pG.setColor( Resources.COLOR_SHADOW );
        pG.drawRect( mLeftX + 1,
                     mTopY + 1,
                     mWidth - 1,
                     mHeight - 1 );

        // Draw background box
        pG.setColor( Resources.COLOR_BG_OTHER );
        pG.fillRect( mLeftX,
                     mTopY,
                     mWidth,
                     mHeight );

        // Draw message
        mText.draw( pG );
    }
    
    private static final int getTextWidth( final Font pFont, final String pText )
    {
        return pFont.stringWidth( pText );
    }
    
    private static final int getTextHeight( final Font pFont )
    {
        return pFont.getHeight();
    }
}
