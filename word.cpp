#include<bits/stdc++.h>
using namespace std;

bool checkDiv(char tmp){
    return tmp==' '||tmp=='\n'||tmp=='\t';
}

bool checkIdent(string s){
    if(isdigit(s[0]))return false;
    for(auto i:s){
        if((i!='_')&&('0'>i||i>'9')&&('A'>i||i>'Z')&&('a'>i||i>'z'))return false;
    }
    return true;
}

bool checkNumber(string s){
    for(auto i:s){
        if('0'>i||i>'9')return false;
    }
    return true;
}

bool checkCond(string s){
    if(s=="if"||s=="else"||s=="while"||s=="break"
       ||s=="continue"||s=="return")return true;
    return false;
}

bool checkSign(string s){
    if(s=="="||s=="=="||s==";"||s=="("
       ||s==")"||s=="{"||s=="}"||s=="+"||s=="*"
       ||s=="/"||s=="<"||s==">")return true;
    return false;
}

bool check(string s){
    if(s.empty())return false;
    if(checkCond(s)||checkIdent(s)||checkNumber(s)||checkSign(s))return true;
    return false;
}

void output(string s){
    if(checkCond(s)){
        s[0]=toupper(s[0]);
        cout<<s<<endl;
    }
    else if(checkSign(s)){
        if(s=="=")cout<<"Assign\n";
        else if(s==";")cout<<"Semicolon\n";
        else if(s=="(")cout<<"LPar\n";
        else if(s==")")cout<<"RPar\n";
        else if(s=="{")cout<<"LBrace\n";
        else if(s=="}")cout<<"RBrace\n";
        else if(s=="+")cout<<"Plus\n";
        else if(s=="*")cout<<"Mult\n";
        else if(s=="/")cout<<"Div\n";
        else if(s=="<")cout<<"Lt\n";
        else if(s==">")cout<<"Gt\n";
        else if(s=="==")cout<<"Eq\n";
        else{
            cout<<"Err\n";
            exit(0);
        }
    }else if(checkIdent(s)){
        cout<<"Ident("<<s<<")\n";
    }else if(checkNumber(s)){
        cout<<"Number("<<s<<")\n";
    }else{
        cout<<"Err\n";
        exit(0);
    }
}

int main() {
    string t;
    ios_base::sync_with_stdio(false);
    cin.tie(0);cout.tie(0);
    while(getline(cin,t)){
        string s;
        s.clear();
        for(int j=0;j<t.size();j++){
            auto i=t[j];
            string tmp=s+i;
            if(checkDiv(i)){
                if(!s.empty()){
                    if(check(s))output(s);
                    else {
                        cout<<"Err\n";
                        exit(0);
                    }
                }
                s.clear();
            }else if(!check(tmp)){
                if(check(s))output(s);
                else {
                    cout<<"Err\n";
                    exit(0);
                }
                s.clear();
                s+=i;
                if(j==t.size()-1){
                    if(!s.empty()){
                        if(check(s))output(s);
                        else {
                            cout<<"Err\n";
                            exit(0);
                        }
                    }
                    s.clear();
                }
            }else{
                s+=i;
                if(j==t.size()-1){
                    if(!s.empty()){
                        if(check(s))output(s);
                        else {
                            cout<<"Err\n";
                            exit(0);
                        }
                    }
                    s.clear();
                }
            }
        }
    }
    return 0;
}