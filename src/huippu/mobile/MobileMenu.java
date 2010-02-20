package huippu.mobile;

import huippu.common.Resources;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;

final class MobileMenu
    implements CommandListener
{
    private final Command mCmdNextLevel =
        new Command( Resources.TEXT_NEXT_LEVEL, Command.SCREEN, 1 );
    private final Command mCmdRestart =
        new Command( Resources.TEXT_NEW_GAME, Command.SCREEN, 1 );
    private final Command mCmdViewHallOfFame =
        new Command( Resources.TEXT_VIEW_HOF, Command.SCREEN, 2 );
    private final Command mCmdExit =
        new Command( Resources.TEXT_EXIT, Command.EXIT, 5 );
    
    private final MobileMain mApplication;
    private final MobileDrome mDrome;
    
    public MobileMenu( final MobileMain pApplication, final MobileDrome pDrome )
    {
        mApplication = pApplication;
        mDrome = pDrome;
        
        mDrome.addCommand( mCmdRestart );
        mDrome.addCommand( mCmdViewHallOfFame );
        mDrome.addCommand( mCmdExit );
        mDrome.setCommandListener( this );
    }
    
    final void setNextLevelEnabled( final boolean pEnabled )
    {
        if ( pEnabled )
        {
            mDrome.removeCommand( mCmdRestart );
            mDrome.addCommand( mCmdNextLevel );
        }
        else
        {
            mDrome.removeCommand( mCmdNextLevel );
            mDrome.addCommand( mCmdRestart );
        }
    }

    public final void commandAction( final Command pC,
                                     final Displayable pD )
    {
        if ( pC == mCmdRestart )
        {
            mDrome.dromeFinished( false, false );
            mDrome.restart();
        }
        else if ( pC == mCmdNextLevel )
        {
            mDrome.startNextLevel();
        }
        else if ( pC == mCmdViewHallOfFame )
        {
            mDrome.viewHallOfFame();
        }
        else if ( pC == mCmdExit )
        {
            mDrome.gameExiting();
            mApplication.exitApplication();
        }
    }
}
