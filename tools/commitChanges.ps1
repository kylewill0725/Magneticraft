Param (
    [Parameter(Position=1, Mandatory=$true)][string]$path,
    [Parameter(Position=2, Mandatory=$false)][switch]$amend
)
Set-Location $path
$buildNum = git rev-list --count HEAD
$buildNum -= 134
git add *
if ($amend) {
    if (-Not (Test-Path .\message.txt) -Or ((Get-Content .\message.txt -Raw).length -lt 1)) {
        if (-Not (Test-Path .\message.txt)) {
            Invoke-Expression -Command ".\tools\makeMessageFile.ps1 $path"
        }
        Throw "Please fill in message.txt"
    }
    Write-Host "git commit --amend --file=.\message.txt"
    Write-Host ((git commit --amend --file=.\message.txt) -join "`r`n")
    Remove-Item .\message.txt
} else {
    Remove-Item .\message.txt -ErrorAction SilentlyContinue
    Write-Host "git commit -m ""Build $buildNum"""
    Write-Host ((git commit -m "Build $buildNum") -join "`r`n")
}
