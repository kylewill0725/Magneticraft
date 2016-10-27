Param (
    [Parameter(Position=1, Mandatory=$true)][string]$path
)
Set-Location $path
$buildNum = git rev-list --count HEAD
$buildNum -= 135
New-Item ".\message.txt"
$message = ((git log -1 --pretty=%B)  -join "`r`n")
Add-Content ".\message.txt" "$message"