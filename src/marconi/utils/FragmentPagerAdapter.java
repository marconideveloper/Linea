/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package marconi.utils;

import javafx.scene.layout.Pane;

/**
 *
 * @author felipe
 */
public abstract class FragmentPagerAdapter extends PagerAdapter {

    private final FragmentManager fm;
    private FragmentTransaction currentTransaction;
    private Fragment currentPrimaryFragment;

    public FragmentPagerAdapter(FragmentManager fm) {
        this.fm = fm;
    }

    public abstract Fragment getItem(int pos);

    @Override
    public Object instantiateItem(Pane container, int position) {
        if (this.currentTransaction == null) {
            this.currentTransaction = this.fm.beginTransaction();
        }
        final long itemId = getItemId(position);

        String name = makeFragmentName(container.getId(), itemId);
        Fragment fragment = fm.findFragmentByTag(name);
        if (fragment != null) {
            currentTransaction.attach(fragment);
        } else {
            fragment=getItem(position);
            currentTransaction.add(container.getId(), fragment, name);
        }
        return fragment;
    }
    
    public long getItemId(int position) {
        return position;
    }
   
    private static String makeFragmentName(String viewId, long id) {
        return "marconi:switcher:" + viewId + ":" + id;
    }
}
