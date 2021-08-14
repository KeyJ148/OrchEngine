package cc.abro.orchengine.profiles;

public class Profiles {

    private static final String ENV_NAME = "ORCHENGINE_PROFILE";
    private static final Profile DEFAULT_PROFILE = Profile.LOCAL;

    private static volatile Profile activeProfile;

    public static synchronized void initProfile(Profile profile) {
        if (activeProfile == null){
            activeProfile = profile;
        } else {
            throw new IllegalStateException("Profile must be initialized before first getting it");
        }
    }

    public static Profile getActiveProfile() {
        if (activeProfile == null) {
            synchronized (Profile.class) {
                if (activeProfile == null){
                    activeProfile = getProfileFromEnv();
                }
            }
        }
        return activeProfile;
    }

    private static Profile getProfileFromEnv() {
        try {
            return Profile.valueOf(System.getenv(ENV_NAME));
        } catch (IllegalArgumentException | NullPointerException e) {
            return DEFAULT_PROFILE;
        }
    }
}
