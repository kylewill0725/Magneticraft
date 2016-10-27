Set-Location $PSScriptRoot
#Go to root of project
Set-Location "./.."
New-Item -ItemType Directory "./run/resourcepacks/test_pack"
Set-Location "./run/resourcepacks/test_pack"
New-Item -ItemType File "./pack.mcmeta"
Add-Content "./pack.mcmeta" @"
{
   "pack":{
      "pack_format":3,
      "description":"My Resource Pack"
   }
}
"@
#Go back to root
Set-Location "./../../.."
New-Item -ItemType Junction -Name "./run/resourcepacks/test_pack/assets" -Target "./src/main/resources/assets"
