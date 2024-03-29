plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}
rootProject.name = "beu-skripsi-backend"

include("core")
include("plugins")
include("auth")
include("middleware")
include("recipe")
include("comment")
include("history")
include("review")
include("favorite")
include("profile")
include("common")
include("static_resources")
include("help_center")
