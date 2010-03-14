package huippu.mobile;

import huippu.common.Resources;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

final class MobileText
{
    static final Font DEFAULT_FONT =
        Font.getFont( Font.FACE_PROPORTIONAL, Font.STYLE_PLAIN, Font.SIZE_SMALL );

    static final Font FONT_BOLD_LARGE =
        Font.getFont( Font.FACE_PROPORTIONAL, Font.STYLE_BOLD, Font.SIZE_MEDIUM );
    
    private final int mX;
    private final int mY;
    private final Font mFont;
    private final int mAnchor;
    private String mText;
    
    public MobileText( final String pText, final int pX, final int pY,
                       final int pAnchor )
    {
        this( pText, pX, pY, pAnchor, DEFAULT_FONT );
    }
    
    public MobileText( final String pText, final int pX, final int pY,
                       final int pAnchor, final Font pFont )
    {
        mX = pX;
        mY = pY;
        mAnchor = pAnchor;
        mFont = pFont;
        mText = pText;
    }
    
    final void setText( final String pText )
    {
        mText = pText;
    }
    
    final int draw( final Graphics pG )
    {
        pG.setFont( mFont );
        
        pG.setColor( Resources.COLOR_SHADOW );
        pG.drawString( mText, mX + 1, mY + 1, mAnchor );

        pG.setColor( Resources.COLOR_TEXT );
        pG.drawString( mText, mX, mY, mAnchor );

        return pG.getFont()
                 .stringWidth( mText );
    }
}
