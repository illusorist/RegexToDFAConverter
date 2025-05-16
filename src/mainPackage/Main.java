package mainPackage;

public class Main {
    public static void main(String[] args) throws Exception {

        // ----- test for mainPackage.NFA Class --------

        NFAtoDFA.convertAndPrint(RegexToNFA.convertRegexToNFA("((a)(ab)) | ((aa)(ab))"));

    }
}