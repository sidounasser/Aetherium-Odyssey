$ErrorActionPreference = "Stop"
$jdk = "C:\Users\sidou\.jdks\jdk-21.0.13+9\bin"
$lib = "$env:APPDATA\.minecraft\libraries"
$proj = "c:\Users\sidou\Desktop\Ai Project"
$out = "$proj\build\forge"

Remove-Item -Recurse -Force $out -ErrorAction SilentlyContinue
New-Item -ItemType Directory -Force -Path "$out\classes", "$out\jar" | Out-Null

# Build classpath: all libraries except other Forge/MC versions
$jars = Get-ChildItem $lib -Recurse -Filter "*.jar" | Where-Object {
    $_.FullName -notmatch "1\.21\.10" -and
    $_.FullName -notmatch "eventbus\\7\." -and
    $_.FullName -notmatch "JarJar.*0\.4\." -and
    $_.FullName -notmatch "natives"
} | ForEach-Object { $_.FullName }
$forgeFirst = @(
    "$lib\net\minecraftforge\forge\1.21.1-52.1.14\forge-1.21.1-52.1.14-client.jar",
    "$lib\net\minecraftforge\forge\1.21.1-52.1.14\forge-1.21.1-52.1.14-universal.jar"
)
$jars = $forgeFirst + ($jars | Where-Object { $forgeFirst -notcontains $_ -and $_ -notmatch "net\\minecraft\\client\\" })
$cpFile = "$out\cp.txt"
($jars -join ";") | Set-Content -Path $cpFile -Encoding ASCII

$sources = Get-ChildItem "$proj\forge-src" -Recurse -Filter "*.java" | ForEach-Object { '"' + $_.FullName.Replace('\','/') + '"' }
$srcFile = "$out\sources.txt"
$sources | Set-Content -Path $srcFile -Encoding ASCII

$argFile = "$out\javac.args"
@(
    "-encoding", "UTF-8",
    "--release", "21",
    "-proc:none",
    "-nowarn",
    "-d", ('"' + "$out\classes".Replace('\','/') + '"'),
    "-cp", ('"' + ((Get-Content $cpFile -Raw).Trim().Replace('\','/')) + '"')
) | Set-Content -Path $argFile -Encoding ASCII

& "$jdk\javac.exe" "@$argFile" "@$srcFile"
if ($LASTEXITCODE -ne 0) { throw "javac failed" }

# Copy resources (assets + data) but not Fabric metadata
Copy-Item -Recurse "$proj\src\main\resources\assets" "$out\classes\assets"
Copy-Item -Recurse "$proj\src\main\resources\data" "$out\classes\data"
Copy-Item "$proj\LICENSE" "$out\classes\LICENSE_aetherium"

New-Item -ItemType Directory -Force -Path "$out\classes\META-INF" | Out-Null
$modsToml = @"
modLoader="javafml"
loaderVersion="[52,)"
license="MIT"

[[mods]]
modId="aetherium"
version="3.0.0"
displayName="Aetherium Odyssey"
authors="Sidou & Antigravity"
description='''
Mod RPG, Magie & Boss ultra complet : minerais Aetherium/Pyrosite/Neantite, armes legendaires, sceptres, reliques, 3 armures, blocs rituels, dimension celeste, monture et le Boss l'Archonte d'Aetherium !
'''

[[dependencies.aetherium]]
modId="forge"
mandatory=true
versionRange="[52,)"
ordering="NONE"
side="BOTH"

[[dependencies.aetherium]]
modId="minecraft"
mandatory=true
versionRange="[1.21.1,1.21.2)"
ordering="NONE"
side="BOTH"
"@
$utf8NoBom = [System.Text.UTF8Encoding]::new($false)
[System.IO.File]::WriteAllText("$out\classes\META-INF\mods.toml", $modsToml, $utf8NoBom)

$packMeta = @'
{
  "pack": {
    "description": "Aetherium Odyssey resources",
    "pack_format": 34
  }
}
'@
[System.IO.File]::WriteAllText("$out\classes\pack.mcmeta", $packMeta, $utf8NoBom)

$manifest = "Manifest-Version: 1.0`r`nImplementation-Title: aetherium`r`nImplementation-Version: 3.0.0`r`n`r`n"
Set-Content -Path "$out\MANIFEST.MF" -Value $manifest -Encoding ASCII -NoNewline

$jarPath = "$out\jar\aetherium-odyssey-forge-1.21.1-3.0.0.jar"
& "$jdk\jar.exe" --create --file $jarPath --manifest "$out\MANIFEST.MF" -C "$out\classes" .
if ($LASTEXITCODE -ne 0) { throw "jar failed" }

Copy-Item $jarPath "$proj\aetherium-odyssey-forge-1.21.1-3.0.0.jar" -Force
Write-Host "FORGE BUILD OK: $jarPath"
