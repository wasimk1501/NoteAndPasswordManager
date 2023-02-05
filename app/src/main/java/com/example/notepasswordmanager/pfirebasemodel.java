package com.example.notepasswordmanager;

public class pfirebasemodel {
    public String ptitle;
    public String pid;
    public String ppassword;

    public pfirebasemodel() {
    }

    public pfirebasemodel(String ptitle, String pid, String ppassword) {
        this.ptitle = ptitle;
        this.pid = pid;
        this.ppassword = ppassword;
    }

    public String getPtitle() {
        return ptitle;
    }

    public void setPtitle(String ptitle) {
        this.ptitle = ptitle;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getPpassword() {
        return ppassword;
    }

    public void setPpassword(String ppassword) {
        this.ppassword = ppassword;
    }
}
