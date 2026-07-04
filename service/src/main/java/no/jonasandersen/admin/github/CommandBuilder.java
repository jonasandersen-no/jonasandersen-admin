//package no.jonasandersen.admin.github;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class CommandBuilder {
//
//    public static String buildRsync(CommandConfig cfg) {
//        List<String> parts = new ArrayList<>();
//        parts.add("rsync");
//
//        if (cfg.getOptions().isDelete()) parts.add("--delete");
//        if (cfg.getOptions().isArchive()) parts.add("-a");
//        if (cfg.getOptions().isVerbose()) parts.add("-v");
//
//        if (cfg.getOptions().getExcludeFile() != null) {
//            parts.add("--exclude-from='" + cfg.getOptions().getExcludeFile() + "'");
//        }
//
//        parts.add(cfg.getSource());
//        parts.add(cfg.getDestination());
//
//        return String.join(" ", parts);
//    }
//}
