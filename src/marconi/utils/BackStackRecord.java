/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package marconi.utils;

/**
 *
 * @author felipe
 */
public class BackStackRecord extends FragmentTransaction {
    
    static final int ATTACH=7;
    static final int ADD=1;
    
    final FragmentManagerImpl fm;
    Op head, tail;
    int numOp;

    public BackStackRecord(FragmentManagerImpl fm) {
        this.fm = fm;
    }

    @Override
    public FragmentTransaction attach(Fragment f) {
        Op op = new Op();
        op.fragment = f;
        op.cmd=ATTACH;
        addOp(op);
        return this;
    }
     @Override
    public FragmentTransaction add(String containerId, Fragment f, String tag) {
         doAddOp(containerId, f, tag,ADD);
        return this;
    }


    private void addOp(Op op) {
        if (head == null) {
            head=tail=op;
        }else{
        op.prev=tail;
        tail.next=op;
        tail=op;}
        numOp++;
        
    }
    
    private void doAddOp(String containerId, Fragment f, String tag, int cmd){
         f.fragmentManager=fm;
         if(tag!=null){
             f.tag=tag;
         }
         if(!containerId.equals("")){
            f.fragmentId=f.containerid=containerId;
         }
         Op op=new Op();
         op.fragment=f;
         op.cmd=cmd;
         addOp(op);
    }

   
    static final class Op {

        Op next;
        Op prev;
        int cmd;
        Fragment fragment;
    }

}
